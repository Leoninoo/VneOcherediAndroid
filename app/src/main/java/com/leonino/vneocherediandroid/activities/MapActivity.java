package com.leonino.vneocherediandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.leonino.vneocherediandroid.R;
import com.leonino.vneocherediandroid.enums.Constants;
import com.leonino.vneocherediandroid.res.Addresses;
import com.leonino.vneocherediandroid.service.MyLocationListener;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.LinearRing;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polygon;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolygonMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;

public class MapActivity extends AppCompatActivity {
    private static final Point START_POINT = new Point(51.817760, 55.176979);
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private PlacemarkMapObject placemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("66f0fc27-b30d-4882-bbbc-f87715f4172a");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_map);

        MyLocationListener.SetUpLocationListener(this);

        mapView = findViewById(R.id.yaMap);
        mapView.getMap().move(
                new CameraPosition(START_POINT, 14.8f, 0f, 0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        mapObjects = mapView.getMap().getMapObjects().addCollection();
        createObjects();

        mapView.getMap().addTapListener(objectTapListener);

        SearchableSpinner deliveryStreetView = findViewById(R.id.delivery_street);

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                        Addresses.all.values().toArray());

        deliveryStreetView.setAdapter(arrayAdapter);

        TextView priceView = findViewById(R.id.total_price_map);
        String totalPrice = getIntent().getStringExtra(Constants.TOTAL_PRICE);

        priceView.setText(totalPrice);

        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.show_map_button).setOnClickListener(view -> {
                    findViewById(R.id.delivery_box).setVisibility(View.INVISIBLE);
                    findViewById(R.id.location_button).setVisibility(View.VISIBLE);
                });

        findViewById(R.id.map_back).setOnClickListener(view -> {
            if(findViewById(R.id.delivery_box).getVisibility() == View.VISIBLE) {
                finish();
            }
            else {
                findViewById(R.id.delivery_box).setVisibility(View.VISIBLE);
                findViewById(R.id.location_button).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.location_button).setOnClickListener(onClickLocation);
    }

    private final View.OnClickListener onClickLocation = view -> {
        Location location = MyLocationListener.imHere;

        Point point = new Point(location.getLatitude(), location.getLongitude());

        setPlacemark(point);
    };

    private final GeoObjectTapListener objectTapListener = geoObjectTapEvent -> {
        Point point = geoObjectTapEvent.getGeoObject().getGeometry().get(0).getPoint();

        setPlacemark(point);

        return false;
    };

    private void setPlacemark(Point point) {
        String address = "";

        Point finalPoint = point != null ? point : START_POINT;

        if (point != null) {
            double distance = 100000;

            for (Map.Entry<String, String> entry : Addresses.all.entrySet()) {
                String[] coords = entry.getKey().split(", ");

                double addressDistance = Math.sqrt(Math.pow(point.getLatitude() - Double.parseDouble(coords[0]), 2)
                        + Math.pow(point.getLongitude() - Double.parseDouble(coords[1]), 2));

                if (distance > addressDistance) {
                    distance = addressDistance;
                    address = entry.getValue();
                    finalPoint = new Point(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
                }
            }
        }

        if(placemark == null)
            placemark = mapObjects.addPlacemark(finalPoint,
                    ImageProvider.fromResource(MapActivity.this, R.drawable.locate_icon));
        else
            placemark.setGeometry(finalPoint);


        mapView.getMap().move(
                new CameraPosition(finalPoint, 18, 0f, 0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);
    }

    private void createObjects() {
        ArrayList<Point> trianglePoints = new ArrayList<>();
        trianglePoints.add(new Point(85, -179.99));
        trianglePoints.add(new Point(85, 179.99));
        trianglePoints.add(new Point(-85, 179.99));
        trianglePoints.add(new Point(-85, -179.99));
        trianglePoints.add(new Point(85, -179.99));

        ArrayList<Point> listPoints = new ArrayList<>();
        listPoints.add(new Point(51.822871, 55.160999));
        listPoints.add(new Point(51.814823, 55.169647));
        listPoints.add(new Point(51.813114, 55.166302));
        listPoints.add(new Point(51.807372, 55.171846));
        listPoints.add(new Point(51.810514, 55.179704));
        listPoints.add(new Point(51.812484, 55.177552));
        listPoints.add(new Point(51.815177, 55.184069));
        listPoints.add(new Point(51.819003, 55.183638));
        listPoints.add(new Point(51.815395, 55.174558));
        listPoints.add(new Point(51.818247, 55.171437));
        listPoints.add(new Point(51.820336, 55.175927));
        listPoints.add(new Point(51.824381, 55.177792));
        listPoints.add(new Point(51.826306, 55.176717));
        listPoints.add(new Point(51.828363, 55.174609));

        ArrayList<LinearRing> rings = new ArrayList<>();
        rings.add(new LinearRing(listPoints));

        Polygon polygon = new Polygon(new LinearRing(trianglePoints), rings);

        PolygonMapObject triangle = mapObjects.addPolygon(polygon);
        triangle.setFillColor(Color.parseColor("#60FFDF4C"));
        triangle.setStrokeColor(Color.BLACK);
        triangle.setStrokeWidth(0.7f);
        triangle.setZIndex(100.0f);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
}