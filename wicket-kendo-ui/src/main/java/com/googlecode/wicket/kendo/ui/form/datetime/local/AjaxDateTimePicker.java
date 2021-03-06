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
package com.googlecode.wicket.kendo.ui.form.datetime.local;

import java.util.Locale;

import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

import com.googlecode.wicket.jquery.core.JQueryBehavior;
import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.ajax.IJQueryAjaxAware;
import com.googlecode.wicket.jquery.core.ajax.JQueryAjaxPostBehavior;
import com.googlecode.wicket.jquery.core.event.IValueChangedListener;
import com.googlecode.wicket.kendo.ui.ajax.OnChangeAjaxBehavior;
import com.googlecode.wicket.kendo.ui.form.datetime.DatePickerBehavior;
import com.googlecode.wicket.kendo.ui.form.datetime.TimePickerBehavior;

/**
 * Provides a datetime-picker based on a {@link AjaxDatePicker} and a {@link AjaxTimePicker}<br/>
 * This ajax version will post both components, using a {@link JQueryAjaxPostBehavior}, when the 'change' javascript method is called.
 *
 * @author Sebastien Briquet - sebfz1
 */
public class AjaxDateTimePicker extends DateTimePicker implements IValueChangedListener
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(AjaxDateTimePicker.class);

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 */
	public AjaxDateTimePicker(String id)
	{
		super(id);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param locale the {@code Locale}
	 */
	public AjaxDateTimePicker(String id, Locale locale)
	{
		super(id, locale);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param datePattern the SimpleDateFormat pattern for the date
	 * @param timePattern the SimpleDateFormat pattern for the time
	 */
	public AjaxDateTimePicker(String id, String datePattern, String timePattern)
	{
		super(id, datePattern, timePattern);
	}

	/**
	 * constructor
	 *
	 * @param id the markup id
	 * @param locale the {@code Locale}
	 * @param datePattern the SimpleDateFormat pattern for the date
	 * @param timePattern the SimpleDateFormat pattern for the time
	 */
	public AjaxDateTimePicker(String id, Locale locale, String datePattern, String timePattern)
	{
		super(id, locale, datePattern, timePattern);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the date {@code IModel}
	 */
	public AjaxDateTimePicker(String id, IModel<LocalDateTime> model)
	{
		super(id, model);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the date {@code IModel}
	 * @param locale the {@code LocalDate}
	 */
	public AjaxDateTimePicker(String id, IModel<LocalDateTime> model, Locale locale)
	{
		super(id, model, locale);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the date {@code IModel}
	 * @param datePattern the SimpleDateFormat pattern for the date
	 * @param timePattern the SimpleDateFormat pattern for the time
	 */
	public AjaxDateTimePicker(String id, IModel<LocalDateTime> model, String datePattern, String timePattern)
	{
		super(id, model, datePattern, timePattern);
	}

	/**
	 * Main constructor
	 *
	 * @param id the markup id
	 * @param model the date {@code IModel}
	 * @param locale the {@code Locale}
	 * @param datePattern the SimpleDateFormat pattern for the date
	 * @param timePattern the SimpleDateFormat pattern for the time
	 */
	public AjaxDateTimePicker(String id, IModel<LocalDateTime> model, Locale locale, String datePattern, String timePattern)
	{
		super(id, model, locale, datePattern, timePattern);
	}

	// Methods //

	/**
	 * Gets a formated value of input(s)<br/>
	 * This method is designed to provide the 'value' argument of {@link IConverter#convertToObject(String, Locale)}
	 *
	 * @param dateInput the date input
	 * @param timeInput the time input
	 * @return a formated value
	 */
	protected String formatInput(String dateInput, String timeInput)
	{
		if (this.isTimePickerEnabled())
		{
			return String.format("%s %s", dateInput, timeInput);
		}

		return dateInput;
	}

	@Override
	public void convertInput()
	{
		final IConverter<LocalDateTime> converter = this.getConverter(LocalDateTime.class);

		String dateInput = this.datePicker.getInput();
		String timeInput = this.timePicker.getInput();

		try
		{
			String value = this.formatInput(dateInput, timeInput);
			this.setConvertedInput(converter.convertToObject(value, this.getLocale()));
		}
		catch (ConversionException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}

			ValidationError error = new ValidationError();
			error.addKey("DateTimePicker.ConversionError"); // wicket6
			error.setVariable("date", dateInput);
			error.setVariable("time", timeInput);

			this.error(error);
		}
	}

	// Properties //

	@Override
	@SuppressWarnings("unchecked")
	public <C> IConverter<C> getConverter(Class<C> type)
	{
		if (LocalDateTime.class.isAssignableFrom(type))
		{
			return (IConverter<C>) AjaxDateTimePicker.newConverter(this.getTextFormat());
		}

		return super.getConverter(type);
	}

	// Events //

	@Override
	public void onValueChanged(IPartialPageRequestHandler handler)
	{
		// noop
	}

	// Factories //

	/**
	 * Gets a new {@link LocalDateTime} {@link IConverter}.
	 * 
	 * @param format the time format
	 * @return the converter
	 */
	private static IConverter<LocalDateTime> newConverter(final String pattern)
	{
		return new IConverter<LocalDateTime>() {

			private static final long serialVersionUID = 1L;

			@Override
			public LocalDateTime convertToObject(String value, Locale locale) throws ConversionException
			{
				try
				{
					return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern, locale));
				}
				catch (DateTimeParseException e)
				{
					throw new ConversionException(e.getMessage(), e);
				}
			}

			@Override
			public String convertToString(LocalDateTime datetime, Locale locale)
			{
				return datetime != null ? datetime.format(DateTimeFormatter.ofPattern(pattern, locale)) : null;
			}
		};
	}

	@Override
	protected DatePicker newDatePicker(String id, IModel<LocalDate> model, Locale locale, String datePattern, Options options)
	{
		return new AjaxDatePicker(id, model, locale, datePattern, options) {

			private static final long serialVersionUID = 1L;

			@Override
			public JQueryBehavior newWidgetBehavior(String selector)
			{
				IValueChangedListener listener = new IValueChangedListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void onValueChanged(IPartialPageRequestHandler handler)
					{
						AjaxDateTimePicker.this.processInput();
						AjaxDateTimePicker.this.onValueChanged(handler);
					}
				};

				return new DatePickerBehavior(selector, this.options, listener) {

					private static final long serialVersionUID = 1L;

					@Override
					protected JQueryAjaxPostBehavior newOnChangeAjaxBehavior(IJQueryAjaxAware source)
					{
						return new OnChangeAjaxBehavior(source, AjaxDateTimePicker.this.datePicker, AjaxDateTimePicker.this.timePicker);
					}
				};
			}
		};
	}

	@Override
	protected TimePicker newTimePicker(String id, IModel<LocalTime> model, Locale locale, String timePattern, Options options)
	{
		return new AjaxTimePicker(id, model, locale, timePattern, options) {

			private static final long serialVersionUID = 1L;

			@Override
			public JQueryBehavior newWidgetBehavior(String selector)
			{
				IValueChangedListener listener = new IValueChangedListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void onValueChanged(IPartialPageRequestHandler handler)
					{
						AjaxDateTimePicker.this.processInput();
						AjaxDateTimePicker.this.onValueChanged(handler);
					}
				};

				return new TimePickerBehavior(selector, this.options, listener) {

					private static final long serialVersionUID = 1L;

					@Override
					protected JQueryAjaxPostBehavior newOnChangeAjaxBehavior(IJQueryAjaxAware source)
					{
						return new OnChangeAjaxBehavior(source, AjaxDateTimePicker.this.datePicker, AjaxDateTimePicker.this.timePicker);
					}
				};
			}
		};
	}
}
