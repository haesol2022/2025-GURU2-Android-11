package com.example.polling

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.polling.BuildConfig
import com.example.polling.R
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_map)


        // BuildConfig에서 네이버 클라이언트 id 가져오기
        val naverApiKey = BuildConfig.NAVER_MAPS_CLIENT_ID

        // NaverMap SDK에 API 키 설정
        NaverMapSdk.getInstance(this).setClient(
            NaverMapSdk.NaverCloudPlatformClient(naverApiKey)
        )

        // 지도 설정
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        // 지도 객체가 준비되면 onMapReady 호출
        mapView.getMapAsync(this)

        // FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 위치 권한이 허용되었는지 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        // 위치 서비스가 활성화되어 있는지 확인
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {
            Toast.makeText(this, "위치 서비스가 비활성화되어 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 위치 업데이트를 요청하기 위해 LocationRequest 생성
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10초마다 업데이트
            fastestInterval = 5000 // 5초
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // 위치 업데이트 콜백
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult?) {
                locationResult?.lastLocation?.let { location ->
                    val latLng = LatLng(location.latitude, location.longitude)
                    val address = getAddressFromLocation(location.latitude, location.longitude)

                    val marker = Marker()
                    marker.position = latLng
                    marker.map = naverMap

                    // 마커 클릭 시 주소 전달 후 종료
                    marker.setOnClickListener {
                        val result = Bundle().apply {
                            putString("address", address) // 선택한 주소 전달
                        }

                        // FragmentResult 설정
                        val fragmentManager = supportFragmentManager
                        fragmentManager.setFragmentResult("mapAddress", result)

                        finish() // MapActivity 종료
                        true
                    }

                    // 지도 중심을 사용자 위치로 이동
                    naverMap.moveCamera(CameraUpdate.scrollTo(latLng))
                }
            }
        }

        // 위치 업데이트 요청
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        // 요청이 실패하면 위치를 가져올 수 없다는 메시지 표시
        fusedLocationClient.lastLocation.addOnFailureListener(this, OnFailureListener {
            Toast.makeText(this, "위치 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        })
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this)
        // 주소를 가져오는 함수 호출
        val addresses: List<android.location.Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        // addresses가 null이 아니고, 비어 있지 않으면 첫 번째 주소를 반환
        return addresses?.firstOrNull()?.getAddressLine(0) ?: "주소를 찾을 수 없습니다."
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허용됨
                Toast.makeText(this, "위치 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 권한 거부됨
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
