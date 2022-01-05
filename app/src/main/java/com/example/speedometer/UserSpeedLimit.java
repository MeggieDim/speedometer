package com.example.speedometer;

import java.util.Date;

public class UserSpeedLimit {
    private float speed;
    private Date timestamp;

    public static class Builder{
        private float speed;
        private Date timestamp;
        public Builder() {}

        public Builder currentTimestamp(Date timestamp){
            this.timestamp = timestamp;
            return this;
        }
        public UserSpeedLimit build() {
            UserSpeedLimit UserSpeedLimit = new UserSpeedLimit();
            UserSpeedLimit.speed = this.speed;
            UserSpeedLimit.timestamp = this.timestamp;
            return UserSpeedLimit;
        }

        public Builder currentspeed(Float speed) {
            this.speed =speed;
            return this;
        }
    }

    private void speedlimit() {}
    public float getSpeed() {return speed;}
    public Date getTimestamp(){return timestamp;}
    @Override
    public String toString(){
        return "Speed Limit Alert: Speed" + speed + ", TimeStamp" + timestamp;
    }

}
