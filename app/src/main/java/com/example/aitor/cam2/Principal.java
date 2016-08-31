package com.example.aitor.cam2;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.Date;

import CAM_PDU_Descriptions.*;


public class Principal {

    private boolean stop_check;
    private int N_genCam;
    private final int T_CHECKCAMGEN = 1000;
    private BasicContainer usrBasicContainer;
    private HighFrequencyContainer usrHighFrequencyContainer;
    private short wBContainerFlag = 0, wHFContainerFlag = 0;
    private Location location;
    private LocationManager locationManager;
    private boolean permissions;
    private Context c;
    private boolean isGPSEnabled;
    boolean isNetworkEnabled;

    public Principal(Context c)
    {
        this.c = c;
    }

    public CAM generarCAM()
    {
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permissions = false;
        }
        else
        {
            permissions = true;
            locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        System.out.println(permissions);

        CAM cur_CAM = new CAM();
        CamParameters cp = new CamParameters();
        CoopAwareness ca = new CoopAwareness();

        cur_CAM.cam = ca;
        cur_CAM.cam.camParameters = cp;

        ItsPduHeader cur_camHeader;
        BasicContainer cur_basicContainer;
        HighFrequencyContainer cur_highFrequencyContainer;
        cur_camHeader = fill_ITSHeader();
        cur_CAM.header = cur_camHeader;
        cur_CAM.cam.generationDeltaTime = (int) get_TimeStamp();
        if (wBContainerFlag == 0) {
            cur_basicContainer = get_basicContainer();//Fill basic container
            cur_CAM.cam.camParameters.basicContainer = cur_basicContainer;
        } else {
            cur_CAM.cam.camParameters.basicContainer = usrBasicContainer;
            wBContainerFlag = 0;
        }
        if (wBContainerFlag == 0) {
            cur_highFrequencyContainer = getHFC();
            cur_CAM.cam.camParameters.highFrequencyContainer = cur_highFrequencyContainer;

        } else {

            cur_CAM.cam.camParameters.highFrequencyContainer = usrHighFrequencyContainer;
            wHFContainerFlag = 0;
        }
        return cur_CAM;
    }

    public HighFrequencyContainer getHFC() {
        BasicVehicleContainerHighFrequency BVCHF = new BasicVehicleContainerHighFrequency();
        Heading h = new Heading();
        Curvature c = new Curvature();
        Speed s = new Speed();
        SpeedConfidence sc = new SpeedConfidence();
        VehicleLength vl = new VehicleLength();
        LongitudinalAcceleration la = new LongitudinalAcceleration();
        YawRate yr = new YawRate();

        BVCHF.heading = h;
        BVCHF.curvature = c;
        BVCHF.speed = s;
        BVCHF.vehicleLength = vl;
        BVCHF.longitudinalAcceleration = la;
        BVCHF.yawRate = yr;

        /*******************CABECERA**********************/
        BVCHF.heading.headingConfidence = 127;

        if (BVCHF.heading.headingConfidence == 127) {
            BVCHF.heading.headingValue = HeadingValue.unavailable;
        }


        /*******************CURVATURE*******************************/
        BVCHF.curvature.curvatureConfidence = CurvatureConfidence.unavailable;

        if (BVCHF.curvature.curvatureConfidence == CurvatureConfidence.unavailable) {
            BVCHF.curvature.curvatureConfidence = CurvatureConfidence.unavailable;
        }
        /**********************SPEED*******************************/

        if (permissions && isGPSEnabled == true && isNetworkEnabled == true)
        {
            BVCHF.speed.speedConfidence = (int)location.getSpeed();
        }
        else
        {
            BVCHF.speed.speedConfidence = SpeedConfidence.unavailable;
        }

        /**********************DRIVEDIRECTION*******************************/
        BVCHF.driveDirection = DriveDirection.unavailable;

        /*********************VEHICLELENGTH********************************/
        BVCHF.vehicleLength.vehicleLengthConfidenceIndication = VehicleLengthConfidenceIndication.unavailable;
        BVCHF.vehicleLength.vehicleLengthValue = VehicleLengthValue.unavailable;

        /*********************VEHICLEWIDTH********************************/
        BVCHF.vehicleWidth = VehicleWidth.unavailable;

        /********************LONGITUDINALACCELERATION********************/
        BVCHF.longitudinalAcceleration.longitudinalAccelerationConfidence = AccelerationConfidence.unavailable;
        BVCHF.longitudinalAcceleration.longitudinalAccelerationValue = LongitudinalAccelerationValue.unavailable;

        /*******************CURVATURECALCULATIONMODE********************/
        BVCHF.curvatureCalculationMode = CurvatureCalculationMode.unavailable;

        /******************YAWRATE************************************/
        BVCHF.yawRate.yawRateConfidence = YawRateConfidence.unavailable;
        BVCHF.yawRate.yawRateValue = YawRateValue.unavailable;

        HighFrequencyContainer hfc = HighFrequencyContainer.basicVehicleContainerHighFrequency(BVCHF);
        return hfc;
    }

    BasicContainer get_basicContainer() {
        BasicContainer basicContainer = new BasicContainer();
        ReferencePosition cur_pos = new ReferencePosition();
        PosConfidenceEllipse pce = new PosConfidenceEllipse();
        Altitude al = new Altitude();

        cur_pos.altitude = al;
        cur_pos.positionConfidenceEllipse = pce;

        basicContainer.referencePosition = cur_pos;
        basicContainer.referencePosition.altitude.altitudeConfidence = AltitudeConfidence.unavailable;
        basicContainer.referencePosition.positionConfidenceEllipse.semiMajorConfidence = -1;
        basicContainer.referencePosition.positionConfidenceEllipse.semiMajorOrientation = -1;
        basicContainer.referencePosition.positionConfidenceEllipse.semiMinorConfidence = -1;

        if(!permissions)
        {
            basicContainer.referencePosition.latitude = -1;//no disponible
            basicContainer.referencePosition.longitude = -1;//no disponible
        }
        else
        {
            if (isGPSEnabled == false || isNetworkEnabled == false)
            {

                basicContainer.referencePosition.latitude = -1;//no disponible
                basicContainer.referencePosition.longitude = -1;//no disponible
            }
            else
            {
                basicContainer.referencePosition.latitude = (int)location.getLatitude();
                basicContainer.referencePosition.longitude = (int)location.getLongitude();
            }
        }
        return basicContainer;
    }

    ItsPduHeader fill_ITSHeader()
    {
        ItsPduHeader header;
        header=new ItsPduHeader();
        header.protocolVersion=1; //1 or 0, see standards
        header.messageID=2;//2=CAM
        header.stationID=(long) 12;//this may be provided by other entities:security,managenment...
        return header;
    }

    long get_TimeStamp()
    {
        Date d = new Date();

        long ms = (d.getTime()-1072915200 ) % 65536;

        return ms;
    }



}

