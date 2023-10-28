package com.example.googlemapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.googlemapsapp.ui.theme.GoogleMapsAppTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleMapsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //MapWithPolygon()
                    CircleMap()
                    //RouteMap()
                }
            }
        }
    }
}


@Composable
fun MapWithPolygon() {
    val polygonPoints = listOf(
        LatLng(37.7749, -122.4194),
        LatLng(37.8049, -122.4400),
        LatLng(37.7949, -122.4100),
        //LatLng(37.7799, -122.4100)
    )

    // Create a mutable state to track whether the polygon is selected
    var isPolygonSelected by remember { mutableStateOf(false) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 13f)
        }
    ) {
        Polygon(
            points = polygonPoints,
            clickable = true,
            fillColor = if (isPolygonSelected) Color.Red else Color.Green,
            strokeColor = Color.Blue,
            strokeWidth = 5f,
            tag = "San Francisco",
            onClick = { polygon ->
                // Handle polygon click event
                isPolygonSelected = true
            }
        )
    }
    // Add a button to reset the selection
    Box(contentAlignment = Alignment.BottomCenter) {
        Button(
            onClick = {
                isPolygonSelected = false
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text("Reset Selection")
        }
    }
}



@Composable
fun RouteMap() {
    // Define the coordinates for a route as a list of LatLng points
    val routeCoordinates = listOf(
        LatLng(38.575764, -121.478851), //Starting point scramento
        LatLng(37.7749, -122.4194), // San Francisco
        LatLng(36.7783, -119.4179), // Waypoint 1
        LatLng(34.0522, -118.2437), // Waypoint 2 (e.g., Los Angeles)
        LatLng(32.7157, -117.1611)  // Ending point (e.g., San Diego)
    )

    // Create a mutable state to track the selected route
    var selectedRoute by remember { mutableStateOf<Route?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(36.7783, -119.4179), 6f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Draw the route polyline
        Polyline(
            points = routeCoordinates,
            clickable = true,
            color = Color.Blue,
            width = 5f,
            tag = CaliforniaRoute,
            onClick = { polyline ->
                // Handle polyline click event
                selectedRoute = polyline.tag as? Route
            }
        )
    }

    // Display information about the selected route
    selectedRoute?.let { route ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.offset(y = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .width(350.dp)
                    .clip(RoundedCornerShape(10))
                    .background(Color.DarkGray)
                    .padding(20.dp)
            ) {
                Text(text = route.name, style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = route.description, style = TextStyle(fontSize = 16.sp))
            }
        }
    }
}

data class Route(val name: String, val description: String)

val CaliforniaRoute = Route(
    name = "California Road Trip",
    description = "Explore the beautiful coast of California on this scenic road trip from San Francisco to San Diego."
)


@Composable
fun CircleMap() {
    // Define the coordinates for circle centers and their associated information
    val circleData = listOf(
        CircleInfo("Park A", LatLng(37.7749, -122.4194), "This is Park A"),
        CircleInfo("Park B", LatLng(36.7783, -119.4179), "This is Park B"),
        CircleInfo("Park C", LatLng(34.0522, -118.2437), "This is Park C")
    )

    // Create a mutable state to track the selected circle
    var selectedCircle by remember { mutableStateOf<CircleInfo?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(36.7783, -119.4179), 11f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Draw clickable circles for each location
        circleData.forEach { circleInfo ->
            Circle(
                center = circleInfo.center,
                clickable = true,
                fillColor = Color.Blue.copy(alpha = 0.3f),
                radius = 5000.0, // Specify the radius in meters
                strokeColor = Color.Black,
                strokeWidth = 2f,
                tag = circleInfo,
                onClick = { circle ->
                    // Handle circle click event
                    selectedCircle = circle.tag as? CircleInfo
                }
            )
        }
    }


    // Display information about the selected circle
    selectedCircle?.let { circle ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.offset(y = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .width(350.dp)
                    .clip(RoundedCornerShape(10))
                    .background(Color.DarkGray)
                    .padding(20.dp)
            ) {
                Text(text = circle.name, style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = circle.description, style = TextStyle(fontSize = 16.sp))
            }
        }
    }
}

data class CircleInfo(val name: String, val center: LatLng, val description: String)


