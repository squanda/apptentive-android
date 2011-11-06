/*
 * ApptentiveActivity.java
 *
 * Created by Sky Kelsey on 2011-09-18.
 * Copyright 2011 Apptentive, Inc. All rights reserved.
 */

package com.apptentive.android.sdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.apptentive.android.sdk.about.AboutController;
import com.apptentive.android.sdk.activity.BaseActivity;
import com.apptentive.android.sdk.model.ApptentiveModel;
import com.apptentive.android.sdk.model.ViewController;
import com.apptentive.android.sdk.survey.SurveyController;

public class ApptentiveActivity  extends BaseActivity {

	private ALog log = new ALog(this.getClass());

	private Module activeModule;

	private ViewController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		activeModule = Module.valueOf(getIntent().getStringExtra("module"));

		// Inflate the wrapper view, and then inflate the content view into it
		switch(activeModule){
			case ABOUT:
				//inflater.inflate(R.layout.apptentive_feedback_new, contentView);
				controller = new AboutController(this);
				break;
			case SURVEY:
//				super.setTheme(android.R.style.Theme_Translucent_NoTitleBar);
				setContentView(R.layout.apptentive_activity);
				LayoutInflater inflater = getLayoutInflater();
				ViewGroup contentView = (ViewGroup) findViewById(R.id.apptentive_activity_content_view);
				contentView.removeAllViews();
				ApptentiveModel model = ApptentiveModel.getInstance();
				if(model.getSurvey() == null){
					finish();
					return;
				}
				inflater.inflate(R.layout.apptentive_survey, contentView);
				controller = new SurveyController(this);
				break;
			default:
				log.w("No Activity specified. Finishing...");
				finish();
				break;
		}
	}

	@Override
	protected void onDestroy() {
		if(controller != null){
			controller.cleanup();
			controller = null;
		}
		super.onDestroy();
	}

	public static enum Module{
		ABOUT,
		SURVEY
	}
}
