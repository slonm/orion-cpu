// Copyright 2009 Thiago H. de Paula Figueiredo
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package br.com.arsmachina.tapestrycrud.base;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.tapestrycrud.CrudPage;
import br.com.arsmachina.tapestrycrud.CrudEditPage;
import br.com.arsmachina.tapestrycrud.CrudListPage;
import br.com.arsmachina.tapestrycrud.components.NewObjectEventLink;
import br.com.arsmachina.tapestrycrud.components.NewObjectPageLink;
import br.com.arsmachina.tapestrycrud.services.PageUtil;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.runtime.Component;

/**
 * Superclass of the {@link NewObjectEventLink} and {@link NewObjectPageLink}.
 * Багобиксы оригинального модуля (Mihail Slobodyanuk)
 * @author Thiago H. de Paula Figueiredo
 */
public abstract class AbstractNewObjectLink {

	private static final String LOWERCASE_CLASSNAME_MESSAGE = "lowercase.classname";

	private static final String NEW_OBJECT_MESSAGE = "link.new.object";

	private static final String NEW_OBJECT_TEMPLATE_MESSAGE = "link.new.object.template";

	@Inject
	private Messages messages;

	@Inject
	private PageUtil requestUtil;

	@Parameter(allowNull = false, defaultPrefix = BindingConstants.MESSAGE)
	@Property(write = false)
	@SuppressWarnings("unused")
	private String label;

	@Inject
	private ComponentResources resources;

	@Inject
	private TapestryCrudModuleService tapestryCrudModuleService;

	private CrudPage<?, ?> page;

	private Boolean lowercaseClassName = null;

	/**
	 * If false (default value), the body of the tag will be ignored and the intertionalized name of
	 * thelisting page is used. If true, then the body of the label element (in the template) is not
	 * ignored.
	 */
	@Parameter(value = "true")
	private boolean ignoreBody;

	@Inject
	private Authorizer authorizer;

	private Element element;

	/**
	 * Single constructor of this class.
	 */
	public AbstractNewObjectLink() {

		if (lowercaseClassName == null) {

			if (messages.contains(LOWERCASE_CLASSNAME_MESSAGE)) {
				lowercaseClassName = Boolean.valueOf(messages.get(LOWERCASE_CLASSNAME_MESSAGE));
			}
			else {
				lowercaseClassName = Boolean.TRUE;
			}

		}

	}

	/**
	 * Method that returns the link to be rendered.
	 *
	 * @return a {@link Link}. It cannot be null.
	 */
	abstract protected Link createLink();

	String defaultLabel() {

		String returnedLabel = null;

		String pageName = requestUtil.getRequestedPageURL();

		pageName = pageName.replace('/', '.');

		final String pageSpecificLabel = NEW_OBJECT_MESSAGE + "." + pageName;

		if (messages.contains(pageSpecificLabel)) {
			returnedLabel = messages.get(pageSpecificLabel);
		}

		if (returnedLabel == null && 
                        (getPage() instanceof CrudEditPage<?,?> || getPage() instanceof CrudListPage<?,?>) &&
                        getPage().getEntityClass()!=null) {

			final String entityLabelMessage = getPage().getEntityClass().getSimpleName() + "-label";

			if (messages.contains(entityLabelMessage)
					&& messages.contains(NEW_OBJECT_TEMPLATE_MESSAGE)) {

				String format = messages.get(NEW_OBJECT_TEMPLATE_MESSAGE);

				String className = messages.get(entityLabelMessage);

				if (lowercaseClassName != null && lowercaseClassName) {
					className = className.toLowerCase();
				}

				returnedLabel = String.format(format, className);

			}

		}

		if (returnedLabel == null) {
			returnedLabel = messages.get(NEW_OBJECT_MESSAGE);
		}

		return returnedLabel;

	}

	private CrudPage<?, ?> getPage() {

		if (page==null){
                        Component container = resources.getPage();
                        if (container instanceof CrudPage<?, ?> == false) {

                                throw new RuntimeException("The crud/"+this.getClass().getSimpleName()+" must be used inside a page "
                                                + "that extends CrudPage");

                        }
                        page = (CrudPage<?, ?>) container;
                }
                return page;

	}

	boolean beginRender(MarkupWriter writer) {

		if (authorizer.canStore(getPage().getEntityClass()) == false) {
			return false;
		}

		Link link = createLink();

		element = writer.element("a", "href", link.toURI(), "class", "t-crud-new-object");

		resources.renderInformalParameters(writer);

		return !ignoreBody;

	}

	void afterRender(MarkupWriter writer) {

		if (element != null) {

			boolean bodyIsBlank = InternalUtils.isBlank(element.getChildMarkup());

			String label;

			if (bodyIsBlank || ignoreBody) {

				final Messages messages = resources.getMessages();

				final String key = NEW_OBJECT_MESSAGE + "."
						+ tapestryCrudModuleService.getEditPageURL(getPage().getEntityClass()).replace('/', '.');

				if (messages.contains(key)) {
					label = messages.get(key);
				}
				else {
					label = messages.get(NEW_OBJECT_MESSAGE);
				}

				writer.write(label);

			}

			writer.end(); // a

		}

	}

}
