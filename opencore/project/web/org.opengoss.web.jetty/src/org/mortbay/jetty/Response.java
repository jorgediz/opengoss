//========================================================================
//$Id: Response.java,v 1.8 2005/11/25 21:01:45 gregwilkins Exp $
//Copyright 2004-2005 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package org.mortbay.jetty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mortbay.io.BufferCache.CachedBuffer;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.log.Log;
import org.mortbay.util.ByteArrayISO8859Writer;
import org.mortbay.util.IO;
import org.mortbay.util.QuotedStringTokenizer;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;
import org.opengoss.web.internal.jetty.WebPluginServletContext;

/* ------------------------------------------------------------ */
/** Response.
 * @author gregw
 * 
 *TODO: modified by Ery Lee
 *
 */
public class Response implements HttpServletResponse
{
    public static final int
        DISABLED=-1,
        NONE=0,
        STREAM=1,
        WRITER=2;

    private static PrintWriter __nullPrintWriter;
    private static ServletOutputStream __nullServletOut;

    static
    {
        try{
            __nullPrintWriter = new PrintWriter(IO.getNullWriter());
            __nullServletOut = new NullOutput();
        }
        catch (Exception e)
        {
            Log.warn(e);
        }
    }

    private HttpConnection _connection;
    private int _status=SC_OK;
    private String _reason;
    private Locale _locale;
    private String _mimeType;
    private CachedBuffer _cachedMimeType;
    private String _characterEncoding;
    private boolean _explicitEncoding;
    private String _contentType;
    private int _outputState;
    private PrintWriter _writer;



    /* ------------------------------------------------------------ */
    /**
     *
     */
    Response(HttpConnection connection)
    {
        _connection=connection;
    }


    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#reset()
     */
    void recycle()
    {
        _status=SC_OK;
        _reason=null;
        _locale=null;
        _mimeType=null;
        _cachedMimeType=null;
        _characterEncoding=null;
        _explicitEncoding=false;
        _contentType=null;
        _outputState=NONE;
        _writer=null;
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
     */
    public void addCookie(Cookie cookie)
    {
        _connection.getResponseFields().addSetCookie(cookie);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
     */
    public boolean containsHeader(String name)
    {
        return _connection.getResponseFields().containsKey(name);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
     */
    public String encodeURL(String url)
    {
        Request request=_connection.getRequest();

        // should not encode if cookies in evidence
        if (url==null || request==null || request.isRequestedSessionIdFromCookie())
            return url;

        // get session;
        HttpSession session=request.getSession(false);

        // no session
        if (session == null)
            return url;

        // invalid session
        String id = session.getId();
        if (id == null)
            return url;

        // TODO Check host and port are for this server

        SessionManager sessionManager = request.getSessionManager();
        String sessionURLPrefix = sessionManager.getSessionURLPrefix();
        // Already encoded
        int prefix=url.indexOf(sessionURLPrefix);
        if (prefix!=-1)
        {
            int suffix=url.indexOf("?",prefix);
            if (suffix<0)
                suffix=url.indexOf("#",prefix);

            if (suffix<=prefix)
                return url.substring(0,prefix+sessionURLPrefix.length())+id;
            return url.substring(0,prefix+sessionURLPrefix.length())+id+
                url.substring(suffix);
        }

        // edit the session
        int suffix=url.indexOf('?');
        if (suffix<0)
            suffix=url.indexOf('#');
        if (suffix<0)
            return url+sessionURLPrefix+id;
        return url.substring(0,suffix)+
            sessionURLPrefix+id+url.substring(suffix);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
     */
    public String encodeRedirectURL(String url)
    {
        return encodeURL(url);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
     */
    public String encodeUrl(String url)
    {
        return encodeURL(url);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
     */
    public String encodeRedirectUrl(String url)
    {
        return encodeURL(url);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
     */
    public void sendError(int code, String message) throws IOException
    {
    	if (_connection.isIncluding())
    		return;
    	
        if (isCommitted())
            Log.warn("Committed before "+code+" "+message);

        reset();
        setStatus(code,message);
        
        if (message==null)
            message=HttpGenerator.getReason(code);

        // If we are allowed to have a body
        if (code!=SC_NO_CONTENT &&
            code!=SC_NOT_MODIFIED &&
            code!=SC_PARTIAL_CONTENT &&
            code>=SC_OK)
        {
            Request request = _connection.getRequest();

            ErrorHandler error_handler = null;
            ServletContext context = request.getContext();
            if (context!=null) {
            	if(context instanceof ContextHandler.Context) {
            		error_handler=((ContextHandler.Context)context).getContextHandler().getErrorHandler();
            	} else if(context instanceof WebPluginServletContext) {
            		error_handler = ((WebPluginServletContext)context).getContextHandler().getErrorHandler();
            	}
            }
            if (error_handler!=null)
            {
                // TODO - probably should reset these after the request?
                request.setAttribute(ServletHandler.__J_S_ERROR_STATUS_CODE,new Integer(code));
                request.setAttribute(ServletHandler.__J_S_ERROR_MESSAGE, message);
                request.setAttribute(ServletHandler.__J_S_ERROR_REQUEST_URI, request.getRequestURI());
                request.setAttribute(ServletHandler.__J_S_ERROR_SERVLET_NAME,request.getServletName()); 
                
                error_handler.handle(null,_connection.getRequest(),this, Handler.ERROR);
            }
            else
            {
                setContentType(MimeTypes.TEXT_HTML_8859_1);
                ByteArrayISO8859Writer writer= new ByteArrayISO8859Writer(2048);
                if (message != null)
                {
                    message= StringUtil.replace(message, "&", "&amp;");
                    message= StringUtil.replace(message, "<", "&lt;");
                    message= StringUtil.replace(message, ">", "&gt;");
                }
                String uri= request.getRequestURI();
                if (uri!=null)
                {
                    uri= StringUtil.replace(uri, "&", "&amp;");
                    uri= StringUtil.replace(uri, "<", "&lt;");
                    uri= StringUtil.replace(uri, ">", "&gt;");
                }
                
                writer.write("<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>\n");
                writer.write("<title>Error ");
                writer.write(Integer.toString(code));
                writer.write(' ');
                if (message==null)
                    message=HttpGenerator.getReason(code);
                writer.write(message);
                writer.write("</title>\n</head>\n<body>\n<h2>HTTP ERROR: ");
                writer.write(Integer.toString(code));
                writer.write("</h2><pre>");
                writer.write(message);
                writer.write("</pre>\n<p>RequestURI=");
                writer.write(uri);
                writer.write("</p>\n<p><i><small><a href=\"http://jetty.mortbay.org\">Powered by jetty://</a></small></i></p>");
                
                for (int i= 0; i < 20; i++)
                    writer.write("\n                                                ");
                writer.write("\n</body>\n</html>\n");
                
                writer.flush();
                setContentLength(writer.size());
                writer.writeTo(getOutputStream());
                writer.destroy();
            }
        }
        else if (code!=SC_PARTIAL_CONTENT)
        {
            _connection.getRequestFields().remove(HttpHeaders.CONTENT_TYPE_BUFFER);
            _connection.getRequestFields().remove(HttpHeaders.CONTENT_LENGTH_BUFFER);
            _characterEncoding=null;
            _mimeType=null;
            _cachedMimeType=null;
        }

        complete();
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#sendError(int)
     */
    public void sendError(int sc) throws IOException
    {
        sendError(sc,null);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
     */
    public void sendRedirect(String location) throws IOException
    {
    	if (_connection.isIncluding())
    		return;
    	
        if (location==null)
            throw new IllegalArgumentException();

        if (!URIUtil.hasScheme(location))
        {
            StringBuffer buf = _connection.getRequest().getRootURL();
            if (location.startsWith("/"))
                buf.append(URIUtil.canonicalPath(location));
            else
            {
                String path=_connection.getRequest().getRequestURI();
                String parent=(path.endsWith("/"))?path:URIUtil.parentPath(path);
                location=URIUtil.canonicalPath(URIUtil.addPaths(parent,location));
                if (!location.startsWith("/"))
                    buf.append('/');
                buf.append(location);
            }

            location=buf.toString();
        }
        resetBuffer();

        setHeader(HttpHeaders.LOCATION,location);
        setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        complete();

    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
     */
    public void setDateHeader(String name, long date)
    {
        if (!_connection.isIncluding())
            _connection.getResponseFields().putDateField(name, date);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
     */
    public void addDateHeader(String name, long date)
    {
        if (!_connection.isIncluding())
            _connection.getResponseFields().addDateField(name, date);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
     */
    public void setHeader(String name, String value)
    {
        if (!_connection.isIncluding())
            _connection.getResponseFields().put(name, value);
    }
    
    /* ------------------------------------------------------------ */
    /*
     */
    public String getHeader(String name)
    {
        return _connection.getResponseFields().getStringField(name);
    }

    /* ------------------------------------------------------------ */
    /* 
     */
    public Enumeration getHeaders(String name)
    {
        Enumeration e = _connection.getResponseFields().getValues(name);
        if (e==null)
            return Collections.enumeration(Collections.EMPTY_LIST);
        return e;
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
     */
    public void addHeader(String name, String value)
    {
        if (!_connection.isIncluding())
            _connection.getResponseFields().add(name, value);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
     */
    public void setIntHeader(String name, int value)
    {
        if (!_connection.isIncluding())
            _connection.getResponseFields().putLongField(name, value);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
     */
    public void addIntHeader(String name, int value)
    {
        if (!_connection.isIncluding())
            _connection.getResponseFields().addLongField(name, value);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(int sc)
    {
        setStatus(sc,null);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
     */
    public void setStatus(int sc, String sm)
    {
        if (!_connection.isIncluding())
        {
            _status=sc;
            _reason=sm;
        }
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        if (_characterEncoding==null)
            _characterEncoding=StringUtil.__ISO_8859_1;
        return _characterEncoding;
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#getContentType()
     */
    public String getContentType()
    {
        return _contentType;
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#getOutputStream()
     */
    public ServletOutputStream getOutputStream() throws IOException
    {
        if (_outputState==DISABLED)
            return __nullServletOut;

        if (_outputState!=NONE && _outputState!=STREAM)
            throw new IllegalStateException("WRITER");

        _outputState=STREAM;
        return _connection.getOutputStream();
    }

    /* ------------------------------------------------------------ */
    public boolean isWriting()
    {
        return _outputState==WRITER;
    }
    
    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#getWriter()
     */
    public PrintWriter getWriter() throws IOException
    {
        if (_outputState==DISABLED)
            return __nullPrintWriter;

        if (_outputState!=NONE && _outputState!=WRITER)
            throw new IllegalStateException("STREAM");

        /* if there is no writer yet */
        if (_writer==null)
        {
            /* get encoding from Content-Type header */
            String encoding = _characterEncoding;

            if (encoding==null)
            {
                /* implementation of educated defaults */
                if(_mimeType!=null)
                    encoding = null; // TODO getHttpContext().getEncodingByMimeType(_mimeType);

                if (encoding==null)
                    encoding = StringUtil.__ISO_8859_1;

                setCharacterEncoding(encoding);
            }

            /* construct Writer using correct encoding */
            _writer = _connection.getPrintWriter(encoding);
        }
        _outputState=WRITER;
        return _writer;
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String encoding)
    {
    	if (_connection.isIncluding())
    		return;
    	
        // TODO throw unsupported encoding exception ???
        
        if (this._outputState==0 && !isCommitted())
        {
            _explicitEncoding=true;

            if (encoding==null)
            {
                // Clear any encoding.
                if (_characterEncoding!=null)
                {
                    _characterEncoding=null;
                    if (_cachedMimeType!=null)
                        _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_cachedMimeType);
                    else
                        _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_mimeType);
                }
            }
            else
            {
                // No, so just add this one to the mimetype
                _characterEncoding=encoding;
                if (_contentType!=null)
                {
                    int i0=_contentType.indexOf(';');
                    if (i0<0)
                    {   
                        _contentType=null;
                        if(_cachedMimeType!=null)
                        {
                            CachedBuffer content_type = _cachedMimeType.getAssociate(_characterEncoding);
                            if (content_type!=null)
                            {
                                _contentType=content_type.toString();
                                _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,content_type);
                            }
                        }
                        
                        if (_contentType==null)
                        {
                            _contentType = _mimeType+"; charset="+QuotedStringTokenizer.quote(_characterEncoding,";= "); 
                            _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                        }
                    }
                    else
                    {
                        int i1=_contentType.indexOf("charset=",i0);
                        if (i1<0)
                        {
                            _contentType = _contentType+" charset="+QuotedStringTokenizer.quote(_characterEncoding,";= "); 
                        }
                        else
                        {
                            int i8=i1+8;
                            int i2=_contentType.indexOf(" ",i8);
                            if (i2<0)
                                _contentType=_contentType.substring(0,i8)+QuotedStringTokenizer.quote(_characterEncoding,";= ");
                            else
                                _contentType=_contentType.substring(0,i8)+QuotedStringTokenizer.quote(_characterEncoding,";= ")+_contentType.substring(i2);
                        }
                        _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                    }
                }
            }
        }
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(int len)
    {
        // Protect from setting after committed as default handling
        // of a servlet HEAD request ALWAYS sets _content length, even
        // if the getHandling committed the response!
        if (isCommitted() || _connection.isIncluding())
        	return;
        _connection.getResponseFields().putLongField(HttpHeaders.CONTENT_LENGTH, len);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setLongContentLength(long len)
    {
        // Protect from setting after committed as default handling
        // of a servlet HEAD request ALWAYS sets _content length, even
        // if the getHandling committed the response!
        if (isCommitted() || _connection.isIncluding())
        	return;
        _connection.getResponseFields().putLongField(HttpHeaders.CONTENT_LENGTH, len);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
     */
    public void setContentType(String contentType)
    {
        if (isCommitted() || _connection.isIncluding())
            return;
        
        // Yes this method is horribly complex.... but there are lots of special cases and
        // as this method is called on every request, it is worth trying to save string creation.
        //
        
        if (contentType==null)
        {
            if (_locale==null)
                _characterEncoding=null;
            _mimeType=null;
            _cachedMimeType=null;
            _contentType=null;
            _connection.getResponseFields().remove(HttpHeaders.CONTENT_TYPE_BUFFER);
        }
        else
        {
            // Look for encoding in contentType
            int i0=contentType.indexOf(';');

            if (i0>0)
            {
                // we have content type parameters
            
                // Extract params off mimetype
                _mimeType=contentType.substring(0,i0).trim();
                _cachedMimeType=MimeTypes.CACHE.get(_mimeType);

                // Look for charset
                int i1=contentType.indexOf("charset=",i0+1);
                if (i1>=0)
                {
                    _explicitEncoding=true;
                    int i8=i1+8;
                    int i2 = contentType.indexOf(' ',i8);

                    if (_outputState==WRITER)
                    {
                        // strip the charset and ignore;
                        if ((i1==i0+1 && i2<0) || (i1==i0+2 && i2<0 && contentType.charAt(i0+1)==' '))
                        {
                            if (_cachedMimeType!=null)
                            {
                                CachedBuffer content_type = _cachedMimeType.getAssociate(_characterEncoding);
                                if (content_type!=null)
                                {
                                    _contentType=content_type.toString();
                                    _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,content_type);
                                }
                                else
                                {
                                    _contentType=_mimeType+"; charset="+_characterEncoding;
                                    _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                                }
                            }
                            else
                            {
                                _contentType=_mimeType+"; charset="+_characterEncoding;
                                _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                            }
                        }
                        else if (i2<0)
                        {
                            _contentType=contentType.substring(0,i1)+" charset="+QuotedStringTokenizer.quote(_characterEncoding,";= ");
                            _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                        }
                        else
                        {
                            _contentType=contentType.substring(0,i1)+contentType.substring(i2)+" charset="+QuotedStringTokenizer.quote(_characterEncoding,";= ");
                            _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                        }
                    }
                    else if ((i1==i0+1 && i2<0) || (i1==i0+2 && i2<0 && contentType.charAt(i0+1)==' '))
                    {
                        // The params are just the char encoding
                        _cachedMimeType=MimeTypes.CACHE.get(_mimeType);
                        _characterEncoding = QuotedStringTokenizer.unquote(contentType.substring(i8));
                        
                        if (_cachedMimeType!=null)
                        {
                            CachedBuffer content_type = _cachedMimeType.getAssociate(_characterEncoding);
                            if (content_type!=null)
                            {
                                _contentType=content_type.toString();
                                _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,content_type);
                            }
                            else
                            {
                                _contentType=contentType;
                                _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                            }
                        }
                        else
                        {
                            _contentType=contentType;
                            _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                        }
                    }
                    else if (i2>0)
                    {
                        _characterEncoding = QuotedStringTokenizer.unquote(contentType.substring(i8,i2));
                        _contentType=contentType;
                        _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                    }
                    else
                    {
                        _characterEncoding = QuotedStringTokenizer.unquote(contentType.substring(i8)); 
                        _contentType=contentType;
                        _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                    }
                }
                else // No encoding in the params.
                {
                    _cachedMimeType=null;
                    _contentType=_characterEncoding==null?contentType:contentType+" charset="+QuotedStringTokenizer.quote(_characterEncoding,";= ");
                    _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                }
            }
            else // No params at all
            {
                _mimeType=contentType;
                _cachedMimeType=MimeTypes.CACHE.get(_mimeType);
                
                if (_characterEncoding!=null)
                {
                    if (_cachedMimeType!=null)
                    {
                        CachedBuffer content_type = _cachedMimeType.getAssociate(_characterEncoding);
                        if (content_type!=null)
                        {
                            _contentType=content_type.toString();
                            _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,content_type);
                        }
                        else
                        {
                            _contentType=_mimeType+"; charset="+QuotedStringTokenizer.quote(_characterEncoding,";= ");
                            _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                        }
                    }
                    else
                    {
                        _contentType=contentType+"; charset="+QuotedStringTokenizer.quote(_characterEncoding,";= ");
                        _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                    }
                }
                else if (_cachedMimeType!=null)
                {
                    _contentType=_cachedMimeType.toString();
                    _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_cachedMimeType);
                }
                else
                {
                    _contentType=contentType;
                    _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,_contentType);
                }
            }   
        }
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#setBufferSize(int)
     */
    public void setBufferSize(int size)
    {
        _connection.getGenerator().increaseContentBufferSize(size);
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#getBufferSize()
     */
    public int getBufferSize()
    {
        return _connection.getGenerator().getContentBufferSize();
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#flushBuffer()
     */
    public void flushBuffer() throws IOException
    {
        _connection.flushResponse();
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#reset()
     */
    public void reset()
    {
        resetBuffer();

        _status=200;
        _reason=null;
        _mimeType=null;
        _cachedMimeType=null;
        _contentType=null;
        _characterEncoding=null;
        _explicitEncoding=false;
        _locale=null;
        _outputState=NONE;
        _writer=null;
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#resetBuffer()
     */
    public void resetBuffer()
    {
        if (isCommitted())
            throw new IllegalStateException("Committed");
        _connection.getGenerator().resetBuffer();
    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#isCommitted()
     */
    public boolean isCommitted()
    {
        return _connection.isResponseCommitted();
    }


    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
     */
    public void setLocale(Locale locale)
    {
        if (locale == null || isCommitted() ||_connection.isIncluding())
            return;

        _locale = locale;
        _connection.getResponseFields().put(HttpHeaders.CONTENT_LANGUAGE_BUFFER,locale.toString().replace('_','-'));

        if (this._outputState!=0 )
            return;

        /* get current MIME type from Content-Type header */
        String type=getContentType();
        if (type==null)
        {
            // servlet did not set Content-Type yet
            // so lets assume default one
            type="application/octet-stream";
        }
        String charset = null;
        ServletContext context = _connection.getRequest().getContext();
     	if(context instanceof ContextHandler.Context) {
     		charset = ((ContextHandler.Context)context).getContextHandler().getLocaleEncoding(locale);
    	} else if(context instanceof WebPluginServletContext) {
    		charset = ((WebPluginServletContext)context).getContextHandler().getLocaleEncoding(locale);
    	}
        if (charset != null && charset.length()>0)
        {
            int semi=type.indexOf(';');
            if (semi<0)
                type += "; charset="+charset;
            else if (!_explicitEncoding)
                type = type.substring(0,semi)+"; charset="+charset;

            _connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER,type);
        }

    }

    /* ------------------------------------------------------------ */
    /*
     * @see javax.servlet.ServletResponse#getLocale()
     */
    public Locale getLocale()
    {
        if (_locale==null)
            return Locale.getDefault();
        return _locale;
    }

    /* ------------------------------------------------------------ */
    /**
     * @return The HTTP status code that has been set for this request. This will be <code>200<code> 
     *    ({@link HttpServletResponse#SC_OK}), unless explicitly set through one of the <code>setStatus</code> methods.
     */
    public int getStatus()
    {
        return _status;
    }

    /* ------------------------------------------------------------ */
    /**
     * @return The reason associated with the current {@link #getStatus() status}. This will be <code>null</code>, 
     *    unless one of the <code>setStatus</code> methods have been called.
     */
    public String getReason()
    {
        return _reason;
    }




    /* ------------------------------------------------------------ */
    /**
     *
     */
    public void complete()
        throws IOException
    {
        _connection.completeResponse();
    }

    /* ------------------------------------------------------------- */
    /**
     * @return the number of bytes actually written in response body
     */
    public long getContentCount()
    {
        if (_connection==null || _connection.getGenerator()==null)
            return -1;
        return _connection.getGenerator().getContentWritten();
    }

    /* ------------------------------------------------------------ */
    public String toString()
    {
        return "HTTP/1.1 "+_status+" "+ _reason +"\n"+
        _connection.getResponseFields().toString();
    }

    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    private static class NullOutput extends ServletOutputStream
    {
        public void write(int b) throws IOException
        {
        }
    }

}
