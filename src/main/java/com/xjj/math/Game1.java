package com.xjj.math;

/**
 * Created by XuJijun on 2018-05-03.
 */
public class Game1 {
    // 0=正面，1=反面
    // get[0][0] = 3,
    // get[0][1] = -2,
    // get[1][0] = -2,
    // get[1][1] = 1,
    private static int[][] rules = {{3, -2},{-2, 1}};

    private static void getFromGame(int times, double ladyRate, double gentlemanRate){
        int ladyZeros = 0;
        int gentlemanZeros = 0;
        int gentlemanGets =0;

        for(int i=0; i<times; i++){
            int ladyCard = Math.random()<ladyRate ? 0 : 1;

            int gentlemanCard = Math.random()<gentlemanRate ? 0 : 1;
            //int gentlemanCard = ladyZeros/(double)i < ladyRate ? 0 : 1;

            if(ladyCard==0){
                ladyZeros++;
            }
            if(gentlemanCard==0){
                gentlemanZeros++;
            }

            //System.out.println("lady=" + ladyCard + "; gentleman=" + gentlemanCard);
            gentlemanGets += rules[ladyCard][gentlemanCard];
        }

        System.out.println("\n女生控制正面的几率=" + ladyRate);
        System.out.println("真正的正面几率：女生=" + ladyZeros/(double)times + "；男生=" + gentlemanZeros/(double)times);
        System.out.println("男生最终获得: " + gentlemanGets + "元");
    }

    public static void main(String[] args){
        double rate0 = 11.0/30.0;
        double rate1 = 1/3.0;
        double rate2 = 2/5.0;
        double rate3 = 0.5;
        double rate4 = 0.8;

        int times = 10000;

        for (int i=1; i<10; i++) {
            getFromGame(times, rate0, 0.1*i);
        }

//        getFromGame(times, rate0, 0.5);
//        getFromGame(times, rate1, 0.5);
//        getFromGame(times, rate2, 0.5);
//        getFromGame(times, rate3, 0.5);
//        getFromGame(times, rate4, 0.5);

    }
}
