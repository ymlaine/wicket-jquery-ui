/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.wicket.jquery.ui.kendo.button;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;

/**
 * Provides a Kendo-ui button based on the built-in Button
 *
 * @author Sebastien Briquet - sebfz1
 *
 */
public class Button extends org.apache.wicket.markup.html.form.Button
{
	private static final long serialVersionUID = 1L;
	private static final String CSS_CLASS = "k-button";

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 */
	public Button(String id)
	{
		super(id);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the {@link IModel}
	 */
	public Button(String id, IModel<String> model)
	{
		super(id, model);
	}

	// Events //
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		this.add(AttributeModifier.replace("class", CSS_CLASS));
	}
}
