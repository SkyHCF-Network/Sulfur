package net.skyhcf.sulfur.positions;

public class CustomRotation
{
    private float yaw;
    private float pitch;

    public CustomRotation( float yaw,  float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }
}

