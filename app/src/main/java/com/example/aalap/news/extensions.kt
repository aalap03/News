package com.example.aalap.news

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun Context.simpleToast(toastMsg: String) = Toasty.info(this, toastMsg, Toast.LENGTH_SHORT).show()