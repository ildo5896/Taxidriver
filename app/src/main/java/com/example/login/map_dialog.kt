package com.example.login

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.jetbrains.annotations.Nullable

class map_dialog : DialogFragment(), OnMapReadyCallback {

    var map: GoogleMap? = null
    private val fragment: SupportMapFragment? = null

    @Nullable
    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.map_dialog, fragment!!).commit()

    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0
    }
}
