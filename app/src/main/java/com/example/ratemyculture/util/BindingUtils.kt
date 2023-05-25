package com.example.ratemyculture.util



import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.example.ratemyculture.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@BindingAdapter("app:src")
fun setImageUri(imageView: CircleImageView, url: String?) {
    if (url != "null") {
        Picasso.get().load(url).into(imageView)
    }else{
        imageView.setImageResource(R.drawable.baseline_person)
    }
}

@BindingAdapter("app:imageBitmap")
fun setImageBitmap(imageView: AppCompatImageView, bitmap: Bitmap?) {
    if (bitmap != null) {
        imageView.setImageBitmap(bitmap)
    }
}

@BindingAdapter("app:obsSrc")
fun setImageViewSrc(imageView: AppCompatImageView, imageUrl: ObservableField<String>) {
    val url = imageUrl.get()
    if (!url.isNullOrEmpty()) {
        Picasso.get().load(url).into(imageView)
    }
}






