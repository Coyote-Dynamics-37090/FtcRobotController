package org.firstinspires.ftc.teamcode.util;


import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

public  class Alliance {

    public enum alliance{
        RED,
        BLUE;
    }

    public static alliance Alliance = alliance.RED;
    private static final PoseFactory p = PoseFactory.degrees();

    public static final Pose redRight = p.of(59.9,21.8,90);
    public static final Pose redLeft = p.of(58.2,111.8,270);
    public static final Pose blueRight = p.of(85.2,115.2,270);
    public static final Pose blueLeft = p.of(85.4,22.7,90);
    public static Pose poseStorage = p.of(0,0,0);

}
