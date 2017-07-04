package com.gilmour.nea.core;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.net.util.SubnetUtils;

/**
 * Created by gilmour on Jul, 2017.
 */
public class IPNumberConverter {

    public static String longToIp(long ip){
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {

            sb.insert(0, Long.toString(ip & 0xff));

            if (i < 3) {
                sb.insert(0, '.');
            }

            // iterations resulting
            // 1. 255.255.255.255
            // 2. 255.255.255
            // 3. 255.255
            // 4. 255
            ip = ip >> 8;

        }

        return sb.toString();
    }


    public static long IpToLong(String ipAddress){
        long result = 0;

        String[] ipAddressInArray = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {

            long ip = Long.parseLong(ipAddressInArray[3 - i]);

            // left shifting 24,16,8,0 and bitwise OR

            // 1. 192 << 24
            // 1. 168 << 16
            // 1. 1 << 8
            // 1. 2 << 0
            result |= ip << (i * 8);

        }
        return result;
    }

    public static Pair<String, String> getIpBlockFromCidrMask(String cidrNotation) throws IllegalArgumentException{
        SubnetUtils utils = new SubnetUtils(cidrNotation);
        return Pair.of(utils.getInfo().getLowAddress(), utils.getInfo().getHighAddress());
    }
}
