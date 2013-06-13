package com.pfe.v4.entities.questions;

import com.pfe.v4.R;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TextQuestion extends Question {
	
	TextView titre;
	EditText texte;
	
	@SuppressWarnings("unused")
	public View getView(){
		int res = R.layout.question_type_text;
		return texte;
		
	}
}
