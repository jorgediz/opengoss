/*
 * Copyright (c) 2003 The Visigoth Software Society. All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Visigoth Software Society (http://www.visigoths.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. Neither the name "FreeMarker", "Visigoth", nor any of the names of the 
 *    project contributors may be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact visigoths@visigoths.org.
 *
 * 5. Products derived from this software may not be called "FreeMarker" or "Visigoth"
 *    nor may "FreeMarker" or "Visigoth" appear in their names
 *    without prior written permission of the Visigoth Software Society.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE VISIGOTH SOFTWARE SOCIETY OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Visigoth Software Society. For more
 * information on the Visigoth Software Society, please see
 * http://www.visigoths.org/
 */

package freemarker.ext.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;
import freemarker.template.utility.Collections12;

/**
 * A class that will wrap a reflected method call into a
 * {@link freemarker.template.TemplateMethodModel} interface. 
 * It is used by {@link BeanModel} to wrap reflected method calls
 * for non-overloaded methods.
 * @author Attila Szegedi, szegedia at users dot sourceforge dot net
 * @version $Id: SimpleMethodModel.java,v 1.27 2005/06/11 12:12:04 szegedia Exp $
 */
final class SimpleMethodModel
    implements
    TemplateMethodModelEx,
    TemplateSequenceModel
{
    private final Object object;
    private final Method method;
    private final Class[] argTypes;
    private final BeansWrapper wrapper;

    /**
     * Creates a model for a specific method on a specific object.
     * @param object the object to call the method on. Can be
     * <tt>null</tt> for static methods.
     * @param method the method that will be invoked.
     */
    SimpleMethodModel(Object object, Method method, Class[] argTypes, 
            BeansWrapper wrapper)
    {
        this.object = object;
        this.method = method;
        this.argTypes = argTypes;
        this.wrapper = wrapper;
    }

    /**
     * Invokes the method, passing it the arguments from the list.
     */
    public Object exec(List arguments)
        throws
        TemplateModelException
    {
        try
        {
            Object[] args = wrapper.unwrapArguments(arguments, argTypes);
            if(args != null) {
                BeansWrapper.coerceBigDecimals(method, args);
            }
            return wrapper.invokeMethod(object, method, args);
        }
        catch(Exception e)
        {
            while(e instanceof InvocationTargetException)
            {
                Throwable t = ((InvocationTargetException)e).getTargetException();
                if(t instanceof Exception)
                {
                    e = (Exception)t;
                }
                else
                {
                    break;
                }
            }
            if((method.getModifiers() & Modifier.STATIC) != 0)
            {
                throw new TemplateModelException("Method " + method + 
                        " threw an exception", e);
            }
            else
            {
                throw new TemplateModelException("Method " + method + 
                        " threw an exception when invoked on " + object, e);
            }
        }
    }

    public TemplateModel get(int index)
        throws
        TemplateModelException
    {
        return (TemplateModel) exec(Collections12.singletonList(new SimpleNumber(new Integer(index))));
    }

    public int size() throws TemplateModelException
    {
        throw new TemplateModelException("?size is unsupported for: " + getClass().getName());
    }
}
