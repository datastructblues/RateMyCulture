package com.example.ratemyculture.feature.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class SignUpVM : ViewModel() {

    var fullName = ObservableField<String>("")
    var email = ObservableField<String>("")
    var password = ObservableField<String>("")
    var passwordConfirm = ObservableField<String>("")

}