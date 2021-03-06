/*
 * Copyright (c) 2011-2014 Fernando Petrola
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dragome.html.dom;

import java.io.Serializable;

import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.services.RequestExecutorImpl;
import com.dragome.services.interfaces.AsyncCallback;

public class Timer
{
	private Object id;

	public Timer setInterval(final Runnable runnable, int milliseconds)
	{
		setupTimer(runnable);
		ScriptHelper.put("milliseconds", milliseconds, this);
		id= ScriptHelper.eval("setInterval('window.wrappedCallback.$onSuccess___java_lang_Object$void(new String())', milliseconds)", this);
		return this;
	}

	private void setupTimer(final Runnable runnable)
	{
		AsyncCallback<String> asyncCallback= new AsyncCallback<String>()
		{
			public void onSuccess(String result)
			{
				runnable.run();
			}

			public void onError()
			{
			}
		};
		AsyncCallback<String> wrappedCallback= RequestExecutorImpl.wrapCallback(Serializable.class, asyncCallback);

		ScriptHelper.put("wrappedCallback", wrappedCallback, this);
		ScriptHelper.eval("window.wrappedCallback=wrappedCallback", this);
	}

	public Timer setTimeout(final Runnable runnable, int milliseconds)
	{
		setupTimer(runnable);
		ScriptHelper.put("milliseconds", milliseconds, this);
		id= ScriptHelper.eval("setTimeout('window.wrappedCallback.$onSuccess___java_lang_Object$void(new String())', milliseconds)", this);
		return this;
	}

	public void clearInterval()
	{
		ScriptHelper.put("interval", id, this);
		ScriptHelper.eval("clearInterval(interval)", this);
	}

}
