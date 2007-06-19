// ========================================================================
// Copyright 2003-2005 Mort Bay Consulting Pty. Ltd.
// ------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.opengoss.web.internal.jetty;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.UnavailableException;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.security.Authenticator;
import org.mortbay.jetty.security.BasicAuthenticator;
import org.mortbay.jetty.security.ClientCertAuthenticator;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.DigestAuthenticator;
import org.mortbay.jetty.security.FormAuthenticator;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.servlet.Dispatcher;
import org.mortbay.jetty.servlet.ErrorPageErrorHandler;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.FilterMapping;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.ServletMapping;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;
import org.mortbay.util.LazyList;
import org.mortbay.util.Loader;
import org.mortbay.xml.XmlParser;
/* ------------------------------------------------------------------------------- */
/**
 * Configure by parsing default web.xml and web.xml
 * 
 * @author gregw
 */
@SuppressWarnings("serial")
public class WebXmlConfiguration implements Configuration
{
    protected WebPluginContext _context;
    protected XmlParser _xmlParser;
    protected Object _filters;
    protected Object _filterMappings;
    protected Object _servlets;
    protected Object _servletMappings;
    protected Object _welcomeFiles;
    protected Object _constraintMappings;
    protected Object _listeners;
    protected Map _errorPages;
    protected boolean _hasJSP;
    protected String _jspServletName;
    protected boolean _defaultWelcomeFileList;

    public WebXmlConfiguration()
    {
        // Get parser
        _xmlParser=webXmlParser();
    }

    public static XmlParser webXmlParser()
    {
        XmlParser xmlParser=new XmlParser();
        //set up cache of DTDs and schemas locally
        URL dtd22=WebPluginContext.class.getResource("/javax/servlet/resources/web-app_2_2.dtd");
        URL dtd23=WebPluginContext.class.getResource("/javax/servlet/resources/web-app_2_3.dtd");
        URL jsp20xsd=WebPluginContext.class.getResource("/javax/servlet/resources/jsp_2_0.xsd");
        URL jsp21xsd=WebPluginContext.class.getResource("/javax/servlet/resources/jsp_2_1.xsd");
        URL j2ee14xsd=WebPluginContext.class.getResource("/javax/servlet/resources/j2ee_1_4.xsd");
        URL webapp24xsd=WebPluginContext.class.getResource("/javax/servlet/resources/web-app_2_4.xsd");
        URL webapp25xsd=WebPluginContext.class.getResource("/javax/servlet/resources/web-app_2_5.xsd");
        URL schemadtd=WebPluginContext.class.getResource("/javax/servlet/resources/XMLSchema.dtd");
        URL xmlxsd=WebPluginContext.class.getResource("/javax/servlet/resources/xml.xsd");
        URL webservice11xsd=WebPluginContext.class.getResource("/javax/servlet/resources/j2ee_web_services_client_1_1.xsd");
        URL webservice12xsd=WebPluginContext.class.getResource("/javax/servlet/resources/javaee_web_services_client_1_2.xsd");
        URL datatypesdtd=WebPluginContext.class.getResource("/javax/servlet/resources/datatypes.dtd");
        xmlParser.redirectEntity("web-app_2_2.dtd",dtd22);
        xmlParser.redirectEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN",dtd22);
        xmlParser.redirectEntity("web.dtd",dtd23);
        xmlParser.redirectEntity("web-app_2_3.dtd",dtd23);
        xmlParser.redirectEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN",dtd23);
        xmlParser.redirectEntity("XMLSchema.dtd",schemadtd);
        xmlParser.redirectEntity("http://www.w3.org/2001/XMLSchema.dtd",schemadtd);
        xmlParser.redirectEntity("-//W3C//DTD XMLSCHEMA 200102//EN",schemadtd);
        xmlParser.redirectEntity("jsp_2_0.xsd",jsp20xsd);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/j2ee/jsp_2_0.xsd",jsp20xsd);
        xmlParser.redirectEntity("jsp_2_1.xsd",jsp21xsd);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/javaee/jsp_2_1.xsd",jsp21xsd);
        xmlParser.redirectEntity("j2ee_1_4.xsd",j2ee14xsd);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/j2ee/j2ee_1_4.xsd",j2ee14xsd);
        xmlParser.redirectEntity("web-app_2_4.xsd",webapp24xsd);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd",webapp24xsd);
        xmlParser.redirectEntity("web-app_2_5.xsd",webapp25xsd);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd",webapp25xsd);
        xmlParser.redirectEntity("xml.xsd",xmlxsd);
        xmlParser.redirectEntity("http://www.w3.org/2001/xml.xsd",xmlxsd);
        xmlParser.redirectEntity("datatypes.dtd",datatypesdtd);
        xmlParser.redirectEntity("http://www.w3.org/2001/datatypes.dtd",datatypesdtd);
        xmlParser.redirectEntity("j2ee_web_services_client_1_1.xsd",webservice11xsd);
        xmlParser.redirectEntity("http://www.ibm.com/webservices/xsd/j2ee_web_services_client_1_1.xsd",webservice11xsd);
        xmlParser.redirectEntity("javaee_web_services_client_1_2.xsd",webservice12xsd);
        xmlParser.redirectEntity("http://www.ibm.com/webservices/xsd/javaee_web_services_client_1_2.xsd",webservice12xsd);

        return xmlParser;
    }

    /* ------------------------------------------------------------------------------- */
    public void setWebPluginContext (WebPluginContext context)
    {
        _context = context;
    }

    /* ------------------------------------------------------------------------------- */
    public WebPluginContext getWebPluginContext()
    {
        return _context;
    }

    /* ------------------------------------------------------------------------------- */
    /** Configure ClassPath.
     */
    public  void configureClassLoader()
    throws Exception
    {
    }

    /* ------------------------------------------------------------------------------- */
    public void configureDefaults() throws Exception
    {
        //cannot configure if the context is already started
        if (_context.isStarted())
        {
            if (Log.isDebugEnabled()){Log.debug("Cannot configure webapp after it is started");}
            return;
        }
        String defaultsDescriptor=getWebPluginContext().getDefaultsDescriptor();
        if(defaultsDescriptor!=null&&defaultsDescriptor.length()>0)
        {
            Resource dftResource=Resource.newSystemResource(defaultsDescriptor);
            if(dftResource==null)
                dftResource=Resource.newResource(defaultsDescriptor);
            configure(dftResource.getURL().toString());
            _defaultWelcomeFileList=_welcomeFiles!=null;
        }
    }

    /* ------------------------------------------------------------------------------- */
    public void configureWebPlugin() throws Exception
    {
        //cannot configure if the context is already started
        if (_context.isStarted())
        {
            if (Log.isDebugEnabled())
		Log.debug("Cannot configure webapp after it is started");
            return;
        }

        URL webxml=findWebXml();
        if (webxml!=null)
            configure(webxml.toString());
    }

    /* ------------------------------------------------------------------------------- */
    protected URL findWebXml() throws IOException, MalformedURLException
    {
    	return getWebPluginContext().getResource("WEB-INF/web.xml");
    }
    
    /* ------------------------------------------------------------------------------- */
    public void configure(String webXml) throws Exception
    {
        XmlParser.Node config=null;
        config=_xmlParser.parse(webXml);
        initialize(config);
    }

    /* ------------------------------------------------------------------------------- */
    public void deconfigureWebPlugin() throws Exception
    {
        // TODO preserve any configuration that pre-existed.

        ServletHandler servlet_handler = getWebPluginContext().getServletHandler();

        servlet_handler.setFilters(null);
        servlet_handler.setFilterMappings(null);
        servlet_handler.setServlets(null);
        servlet_handler.setServletMappings(null);

        getWebPluginContext().setEventListeners(null);
        getWebPluginContext().setWelcomeFiles(null);
        if (getWebPluginContext().getSecurityHandler() != null)
            getWebPluginContext().getSecurityHandler().setConstraintMappings(null);

        if (getWebPluginContext().getErrorHandler() instanceof ErrorPageErrorHandler)
            ((ErrorPageErrorHandler)getWebPluginContext().getErrorHandler()).setErrorPages(null);

        // TODO remove classpaths from classloader
    }

    /* ------------------------------------------------------------ */
    protected void initialize(XmlParser.Node config) throws ClassNotFoundException,UnavailableException
    {
        ServletHandler servlet_handler = getWebPluginContext().getServletHandler();

        // Get any existing servlets and mappings.
        _filters=LazyList.array2List(servlet_handler.getFilters());
        _filterMappings=LazyList.array2List(servlet_handler.getFilterMappings());
        _servlets=LazyList.array2List(servlet_handler.getServlets());
        _servletMappings=LazyList.array2List(servlet_handler.getServletMappings());

        _listeners = LazyList.array2List(getWebPluginContext().getEventListeners());
        _welcomeFiles = LazyList.array2List(getWebPluginContext().getWelcomeFiles());
        _constraintMappings = LazyList.array2List(getWebPluginContext().getSecurityHandler().getConstraintMappings());

        _errorPages = getWebPluginContext().getErrorHandler() instanceof ErrorPageErrorHandler ?
                        ((ErrorPageErrorHandler)getWebPluginContext().getErrorHandler()).getErrorPages():null;

        Iterator iter=config.iterator();
        XmlParser.Node node=null;
        while(iter.hasNext())
        {
            try
            {
                Object o=iter.next();
                if(!(o instanceof XmlParser.Node))
                    continue;
                node=(XmlParser.Node)o;
                String name=node.getTag();
                initWebXmlElement(name,node);
            }
            catch(ClassNotFoundException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                Log.warn("Configuration problem at "+node,e);
                throw new UnavailableException("Configuration problem");
            }
        }

        servlet_handler.setFilters((FilterHolder[])LazyList.toArray(_filters,FilterHolder.class));
        servlet_handler.setFilterMappings((FilterMapping[])LazyList.toArray(_filterMappings,FilterMapping.class));
        servlet_handler.setServlets((ServletHolder[])LazyList.toArray(_servlets,ServletHolder.class));
        servlet_handler.setServletMappings((ServletMapping[])LazyList.toArray(_servletMappings,ServletMapping.class));

        getWebPluginContext().setEventListeners((EventListener[])LazyList.toArray(_listeners,EventListener.class));
        getWebPluginContext().setWelcomeFiles((String[])LazyList.toArray(_welcomeFiles,String.class));
        getWebPluginContext().getSecurityHandler().setConstraintMappings((ConstraintMapping[])LazyList.toArray(_constraintMappings, ConstraintMapping.class));

        if (_errorPages!=null && getWebPluginContext().getErrorHandler() instanceof ErrorPageErrorHandler)
            ((ErrorPageErrorHandler)getWebPluginContext().getErrorHandler()).setErrorPages(_errorPages);

    }

    /* ------------------------------------------------------------ */
    /**
     * Handle web.xml element. This method is called for each top level element within the web.xml
     * file. It may be specialized by derived WebAppHandlers to provide additional
     * configuration and handling.
     *
     * @param element The element name
     * @param node The node containing the element.
     */
    protected void initWebXmlElement(String element,XmlParser.Node node) throws Exception
    {
        if("display-name".equals(element))
            initDisplayName(node);
        else if("description".equals(element))
        {}
        else if("context-param".equals(element))
            initContextParam(node);
        else if("servlet".equals(element))
            initServlet(node);
        else if("servlet-mapping".equals(element))
            initServletMapping(node);
        else if("session-config".equals(element))
            initSessionConfig(node);
        else if("mime-mapping".equals(element))
            initMimeConfig(node);
        else if("welcome-file-list".equals(element))
            initWelcomeFileList(node);
        else if("locale-encoding-mapping-list".equals(element))
            initLocaleEncodingList(node);
        else if("error-page".equals(element))
            initErrorPage(node);
        else if("taglib".equals(element))
            initTagLib(node);
        else if("jsp-config".equals(element))
            initJspConfig(node);
        else if("resource-ref".equals(element))
        {
            if(Log.isDebugEnabled())
                Log.debug("No implementation: "+node);
        }
        else if("security-constraint".equals(element))
            initSecurityConstraint(node);
        else if("login-config".equals(element))
            initLoginConfig(node);
        else if("security-role".equals(element))
            initSecurityRole(node);
        else if("filter".equals(element))
            initFilter(node);
        else if("filter-mapping".equals(element))
            initFilterMapping(node);
        else if("listener".equals(element))
            initListener(node);
        else if("distributable".equals(element))
            initDistributable(node);
        else
        {
            if(Log.isDebugEnabled())
            {
                Log.debug("Element {} not handled in {}",element,this);
                Log.debug(node.toString());
            }
        }
    }

    /* ------------------------------------------------------------ */
    protected void initDisplayName(XmlParser.Node node)
    {
        getWebPluginContext().setDisplayName(node.toString(false,true));
    }

    /* ------------------------------------------------------------ */
    protected void initContextParam(XmlParser.Node node)
    {
        String name=node.getString("param-name",false,true);
        String value=node.getString("param-value",false,true);
        if(Log.isDebugEnabled())
            Log.debug("ContextParam: "+name+"="+value);
        getWebPluginContext().getInitParams().put(name, value);
    }

    /* ------------------------------------------------------------ */
    protected void initFilter(XmlParser.Node node)
    {
        FilterHolder holder= newFilterHolder();
        holder.setName(node.getString("filter-name",false,true));
        holder.setClassName(node.getString("filter-class",false,true));

        Iterator iter=node.iterator("init-param");
        while(iter.hasNext())
        {
            XmlParser.Node paramNode=(XmlParser.Node)iter.next();
            String pname=paramNode.getString("param-name",false,true);
            String pvalue=paramNode.getString("param-value",false,true);
            holder.setInitParameter(pname, pvalue);
        }
        _filters=LazyList.add(_filters,holder);
    }

    /* ------------------------------------------------------------ */
    protected void initFilterMapping(XmlParser.Node node)
    {
        String filter_name=node.getString("filter-name",false,true);



        FilterMapping mapping = new FilterMapping();

        mapping.setFilterName(filter_name);

        ArrayList paths = new ArrayList();
        Iterator iter=node.iterator("url-pattern");
        while(iter.hasNext())
        {
            String p=((XmlParser.Node)iter.next()).toString(false,true);
            paths.add(p);
        }
        mapping.setPathSpecs((String[])paths.toArray(new String[paths.size()]));


        ArrayList names = new ArrayList();
        iter=node.iterator("servlet-name");
        while(iter.hasNext())
        {
            String n=((XmlParser.Node)iter.next()).toString(false,true);
            names.add(n);
        }
        mapping.setServletNames((String[])names.toArray(new String[names.size()]));


        int dispatcher=Handler.DEFAULT;
        iter=node.iterator("dispatcher");
        while(iter.hasNext())
        {
            String d=((XmlParser.Node)iter.next()).toString(false,true);
            dispatcher|=Dispatcher.type(d);
        }
        mapping.setDispatches(dispatcher);

        _filterMappings=LazyList.add(_filterMappings,mapping);
    }

    /* ------------------------------------------------------------ */
    protected void initServlet(XmlParser.Node node) 
    {
        String id=node.getAttribute("id");

        // initialize holder
        String servlet_name=node.getString("servlet-name",false,true);
        String servlet_class=node.getString("servlet-class",false,true);
        ServletHolder holder = newServletHolder();
        holder.setName(servlet_name);

        // init params
        Iterator iParamsIter=node.iterator("init-param");
        while(iParamsIter.hasNext())
        {
            XmlParser.Node paramNode=(XmlParser.Node)iParamsIter.next();
            String pname=paramNode.getString("param-name",false,true);
            String pvalue=paramNode.getString("param-value",false,true);
            holder.setInitParameter(pname,pvalue);
        }
        
        // Handle JSP
        if (id!=null && id.equals("jsp"))
        {
            _jspServletName=servlet_name;
            try
            {
                Loader.loadClass(this.getClass(), servlet_class);
                _hasJSP=true;
            }
            catch(ClassNotFoundException e)
            {
                Log.info("NO JSP Support for {}, did not find {}",_context.getContextPath(),servlet_class);
                _hasJSP=false;
                servlet_class="org.mortbay.servlet.NoJspServlet";
            }
            if (holder.getInitParameter("scratchdir")==null)
            {
                File tmp=getWebPluginContext().getTempDirectory();
                File scratch=new File(tmp,"jsp");
                if (!scratch.exists())
                    scratch.mkdir();
                holder.setInitParameter("scratchdir",scratch.getAbsolutePath());
            }
        }
        holder.setClassName(servlet_class);
        
        // Handler JSP file
        String jsp_file=node.getString("jsp-file",false,true);
        holder.setForcedPath(jsp_file);

        // handle startup
        XmlParser.Node startup=node.get("load-on-startup");
        if(startup!=null)
        {
            String s=startup.toString(false,true).toLowerCase();
            if(s.startsWith("t"))
            {
                Log.warn("Deprecated boolean load-on-startup.  Please use integer");
                holder.setInitOrder(1);
            }
            else
            {
                int order=0;
                try
                {
                    if(s!=null&&s.trim().length()>0)
                        order=Integer.parseInt(s);
                }
                catch(Exception e)
                {
                    Log.warn("Cannot parse load-on-startup "+s+". Please use integer");
                    Log.ignore(e);
                }
                holder.setInitOrder(order);
            }
        }
        Iterator sRefsIter=node.iterator("security-role-ref");
        while(sRefsIter.hasNext())
        {
            XmlParser.Node securityRef=(XmlParser.Node)sRefsIter.next();
            String roleName=securityRef.getString("role-name",false,true);
            String roleLink=securityRef.getString("role-link",false,true);
            if(roleName!=null&&roleName.length()>0&&roleLink!=null&&roleLink.length()>0)
            {
                if(Log.isDebugEnabled())
                    Log.debug("link role "+roleName+" to "+roleLink+" for "+this);
                holder.setUserRoleLink(roleName,roleLink);
            }
            else
            {
                Log.warn("Ignored invalid security-role-ref element: "+"servlet-name="+holder.getName()+", "+securityRef);
            }
        }
        XmlParser.Node run_as=node.get("run-as");
        if(run_as!=null)
        {
            String roleName=run_as.getString("role-name",false,true);
            if(roleName!=null)
                holder.setRunAs(roleName);
        }
        _servlets=LazyList.add(_servlets,holder);
    }

    /* ------------------------------------------------------------ */
    protected void initServletMapping(XmlParser.Node node)
    {
        String servlet_name = node.getString("servlet-name",false,true);
        ServletMapping mapping = new ServletMapping();
        mapping.setServletName(servlet_name);

        ArrayList paths = new ArrayList();
        Iterator iter=node.iterator("url-pattern");
        while(iter.hasNext())
        {
            String p=((XmlParser.Node)iter.next()).toString(false,true);
            paths.add(p);
        }
        mapping.setPathSpecs((String[])paths.toArray(new String[paths.size()]));

        _servletMappings=LazyList.add(_servletMappings,mapping);
    }

    /* ------------------------------------------------------------ */
    protected void initListener(XmlParser.Node node)
    {
        String className=node.getString("listener-class",false,true);
        Object listener=null;
        try
        {
            Class listenerClass=getWebPluginContext().loadClass(className);
            listener=newListenerInstance(listenerClass);
            if(!(listener instanceof EventListener))
            {
                Log.warn("Not an EventListener: "+listener);
                return;
            }
            _listeners=LazyList.add(_listeners, listener);
        }
        catch(Exception e)
        {
            Log.warn("Could not instantiate listener "+className,e);
            return;
        }
    }
    
    /* ------------------------------------------------------------ */
    protected Object newListenerInstance(Class clazz) 
        throws InstantiationException, IllegalAccessException 
    {
                     return clazz.newInstance();
    }
    
    /* ------------------------------------------------------------ */
    protected void initDistributable(XmlParser.Node node)
    {
        // the element has no content, so its simple presence
        // indicates that the webapp is distributable...
        WebPluginContext wac=getWebPluginContext();
        if (!wac.isDistributable())
            wac.setDistributable(true);
    }

    /* ------------------------------------------------------------ */
    protected void initSessionConfig(XmlParser.Node node)
    {
        XmlParser.Node tNode=node.get("session-timeout");
        if(tNode!=null)
        {
            int timeout=Integer.parseInt(tNode.toString(false,true));
            getWebPluginContext().getSessionHandler().getSessionManager().setMaxInactiveInterval(timeout*60);
        }
    }

    /* ------------------------------------------------------------ */
    protected void initMimeConfig(XmlParser.Node node)
    {
        String extension=node.getString("extension",false,true);
        if(extension!=null&&extension.startsWith("."))
            extension=extension.substring(1);
        String mimeType=node.getString("mime-type",false,true);
        getWebPluginContext().getMimeTypes().addMimeMapping(extension, mimeType);
    }

    /* ------------------------------------------------------------ */
    protected void initWelcomeFileList(XmlParser.Node node)
    {
        if (_defaultWelcomeFileList)
            _welcomeFiles=null; // erase welcome files from default web.xml 
        
        _defaultWelcomeFileList=false;
        Iterator iter=node.iterator("welcome-file");
        while(iter.hasNext())
        {
            XmlParser.Node indexNode=(XmlParser.Node)iter.next();
            String welcome=indexNode.toString(false,true);
            _welcomeFiles=LazyList.add(_welcomeFiles,welcome);
        }
    }

    /* ------------------------------------------------------------ */
    protected void initLocaleEncodingList(XmlParser.Node node)
    {
        Iterator iter=node.iterator("locale-encoding-mapping");
        while(iter.hasNext())
        {
            XmlParser.Node mapping=(XmlParser.Node)iter.next();
            String locale=mapping.getString("locale",false,true);
            String encoding=mapping.getString("encoding",false,true);
            getWebPluginContext().addLocaleEncoding(locale,encoding);
        }
    }

    /* ------------------------------------------------------------ */
    protected void initErrorPage(XmlParser.Node node)
    {
        String error=node.getString("error-code",false,true);
        if(error==null||error.length()==0)
            error=node.getString("exception-type",false,true);
        String location=node.getString("location",false,true);

        if (_errorPages==null)
            _errorPages=new HashMap();
        _errorPages.put(error,location);
    }

    /* ------------------------------------------------------------ */
    protected void initTagLib(XmlParser.Node node)
    {
        String uri=node.getString("taglib-uri",false,true);
        String location=node.getString("taglib-location",false,true);

        getWebPluginContext().setResourceAlias(uri,location);
    }

    /* ------------------------------------------------------------ */
    protected void initJspConfig(XmlParser.Node node)
    {
        for (int i=0;i<node.size();i++)
        {
            Object o=node.get(i);
            if (o instanceof XmlParser.Node && "taglib".equals(((XmlParser.Node)o).getTag()))
                initTagLib((XmlParser.Node)o);
        }
        
        // Map URLs from jsp property groups to JSP servlet.
        // this is more JSP stupidness creaping into the servlet spec
        Iterator iter=node.iterator("jsp-property-group");
        Object paths=null;
        while(iter.hasNext())
        {
            XmlParser.Node group=(XmlParser.Node)iter.next();
            Iterator iter2 = group.iterator("url-pattern");
            while (iter2.hasNext())
            {
                String url = ((XmlParser.Node) iter2.next()).toString(false, true);
                paths=LazyList.add(paths,url);
            }
        }

        if (LazyList.size(paths)>0)
        {
            String jspName=getJSPServletName();
            if (jspName!=null)
            {
                ServletMapping mapping = new ServletMapping();
                mapping.setServletName(jspName);
                mapping.setPathSpecs(LazyList.toStringArray(paths));
                _servletMappings=LazyList.add(_servletMappings,mapping);
            }
        }
    }

    /* ------------------------------------------------------------ */
    protected void initSecurityConstraint(XmlParser.Node node)
    {
        Constraint scBase = new Constraint();

        try
        {
            XmlParser.Node auths = node.get("auth-constraint");
            
            if (auths != null)
            {
                scBase.setAuthenticate(true);
                // auth-constraint
                Iterator iter = auths.iterator("role-name");
                Object roles=null;
                while (iter.hasNext())
                {
                    String role = ((XmlParser.Node) iter.next()).toString(false, true);
                    roles=LazyList.add(roles,role);
                }
                scBase.setRoles(LazyList.toStringArray(roles));
            }
            
            XmlParser.Node data = node.get("user-data-constraint");
            if (data != null)
            {
                data = data.get("transport-guarantee");
                String guarantee = data.toString(false, true).toUpperCase();
                if (guarantee == null || guarantee.length() == 0 || "NONE".equals(guarantee))
                    scBase.setDataConstraint(Constraint.DC_NONE);
                else if ("INTEGRAL".equals(guarantee))
                    scBase.setDataConstraint(Constraint.DC_INTEGRAL);
                else if ("CONFIDENTIAL".equals(guarantee))
                    scBase.setDataConstraint(Constraint.DC_CONFIDENTIAL);
                else
                {
                    Log.warn("Unknown user-data-constraint:" + guarantee);
                    scBase.setDataConstraint(Constraint.DC_CONFIDENTIAL);
                }
            }
            Iterator iter = node.iterator("web-resource-collection");
            while (iter.hasNext())
            {
                XmlParser.Node collection = (XmlParser.Node) iter.next();
                String name = collection.getString("web-resource-name", false, true);
                Constraint sc = (Constraint) scBase.clone();
                sc.setName(name);


                Iterator iter2 = collection.iterator("url-pattern");
                while (iter2.hasNext())
                {
                    String url = ((XmlParser.Node) iter2.next()).toString(false, true);

                    Iterator iter3 = collection.iterator("http-method");
                    if (iter3.hasNext())
                    {
                        while (iter3.hasNext())
                        {
                            String method=((XmlParser.Node) iter3.next()).toString(false, true);
                            ConstraintMapping mapping = new ConstraintMapping();
                            mapping.setMethod(method);
                            mapping.setPathSpec(url);
                            mapping.setConstraint(sc);
                            _constraintMappings=LazyList.add(_constraintMappings,mapping);
                        }
                    }
                    else
                    {
                        ConstraintMapping mapping = new ConstraintMapping();
                        mapping.setPathSpec(url);
                        mapping.setConstraint(sc);
                        _constraintMappings=LazyList.add(_constraintMappings,mapping);
                    }
                }
            }
        }
        catch (CloneNotSupportedException e)
        {
            Log.warn(e);
        }

    }

    /* ------------------------------------------------------------ */
    protected void initLoginConfig(XmlParser.Node node)
    {
        XmlParser.Node method=node.get("auth-method");
        FormAuthenticator _formAuthenticator=null;
        if(method!=null)
        {
            Authenticator authenticator=null;
            String m=method.toString(false,true);
            if(Constraint.__FORM_AUTH.equals(m))
                authenticator=_formAuthenticator=new FormAuthenticator();
            else if(Constraint.__BASIC_AUTH.equals(m))
                authenticator=new BasicAuthenticator();
            else if(Constraint.__DIGEST_AUTH.equals(m))
                authenticator=new DigestAuthenticator();
            else if(Constraint.__CERT_AUTH.equals(m))
                authenticator=new ClientCertAuthenticator();
            else if(Constraint.__CERT_AUTH2.equals(m))
                authenticator=new ClientCertAuthenticator();
            else
                Log.warn("UNKNOWN AUTH METHOD: "+m);
            getWebPluginContext().getSecurityHandler().setAuthenticator(authenticator);
        }
        XmlParser.Node name=node.get("realm-name");
        if(name!=null)
        {
            String realm_name=name.toString(false,true);

            UserRealm[] realms=ContextHandler.getCurrentContext().getContextHandler().getServer().getUserRealms();
            UserRealm realm=getWebPluginContext().getSecurityHandler().getUserRealm();
            for (int i=0;realm==null && realms!=null && i<realms.length; i++)
            {
                if (realms[i]!=null && realm_name.equals(realms[i].getName()))
                    realm=realms[i];
            }

            if (realm==null)
            {
                String msg = "Unknown realm: "+realm_name;
                Log.warn(msg);
            }
            else
                getWebPluginContext().getSecurityHandler().setUserRealm(realm);
        }
        XmlParser.Node formConfig=node.get("form-login-config");
        if(formConfig!=null)
        {
            if(_formAuthenticator==null)
                Log.warn("FORM Authentication miss-configured");
            else
            {
                XmlParser.Node loginPage=formConfig.get("form-login-page");
                if(loginPage!=null)
                    _formAuthenticator.setLoginPage(loginPage.toString(false,true));
                XmlParser.Node errorPage=formConfig.get("form-error-page");
                if(errorPage!=null)
                {
                    String ep=errorPage.toString(false,true);
                    _formAuthenticator.setErrorPage(ep);
                }
            }
        }
    }

    /* ------------------------------------------------------------ */
    protected void initSecurityRole(XmlParser.Node node)
    {}

    /* ------------------------------------------------------------ */
    protected ServletHolder newServletHolder() {
        return new ServletHolder();
    }

    /* ------------------------------------------------------------ */
    protected FilterHolder newFilterHolder() {
        return new FilterHolder();
    }
    

    /* ------------------------------------------------------------ */
    protected String getJSPServletName()
    {
        if (_jspServletName==null)
        {
            Map.Entry entry= _context.getServletHandler().getHolderEntry("test.jsp");
            if (entry!=null)
            {
                ServletHolder holder=(ServletHolder)entry.getValue();
                _jspServletName=holder.getName();
            }
        }
        return _jspServletName;
    }

}
