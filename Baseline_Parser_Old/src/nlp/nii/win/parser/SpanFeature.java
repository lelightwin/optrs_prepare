/*
 * To change this license header, choose License Headers in Project Properties
 * To change this template file, choose Tools | Templates
 * and open the template in the editor
 */
package nlp.nii.win.parser;

/**
 *
 * @author lelightwin
 */
public class SpanFeature {

    public static final int quantity = 68;

    // queue feature
    public static final int q0w_q0t = 0;
    public static final int q1w_q1t = 1;
    public static final int q2w_q2t = 2;
    public static final int q3w_q3t = 3;

    // s0's feature
    public static final int s0c_s0ft = 4;
    public static final int s0c_s0fw = 5;
    public static final int s0c_s0lt = 6;
    public static final int s0c_s0lw = 7;
    public static final int s0c_s0at = 8;
    public static final int s0c_s0aw = 9;
    public static final int s0c_s0ft_s0lt = 10;
    public static final int s0c_s0ft_s0lw = 11;
    public static final int s0c_s0fw_s0lt = 12;
    public static final int s0c_s0fw_s0lw = 13;
    public static final int s0c_s0len = 14;

    // s1's feature
    public static final int s1c_s1ft = 15;
    public static final int s1c_s1fw = 16;
    public static final int s1c_s1lt = 17;
    public static final int s1c_s1lw = 18;
    public static final int s1c_s1at = 19;
    public static final int s1c_s1aw = 20;
    public static final int s1c_s1ft_s1lt = 21;
    public static final int s1c_s1ft_s1lw = 22;
    public static final int s1c_s1fw_s1lt = 23;
    public static final int s1c_s1fw_s1lw = 24;
    public static final int s1c_s1len = 25;

    // s0 and s1 's bigram features
    public static final int s1lw_s0fw = 26;
    public static final int s1lw_s0ft = 27;
    public static final int s1lt_s0fw = 28;
    public static final int s1lt_s0ft = 29;

    public static final int s1c_s0fw = 30;
    public static final int s1fw_s0c = 31;
    public static final int s1c_s0lw = 32;
    public static final int s1lw_s0c = 33;

    public static final int s0fw_q0w = 34;
    public static final int s0lw_q0w = 35;
    public static final int s0fw_q0t = 36;
    public static final int s0lw_q0t = 37;
    public static final int s0c_q0w = 38;
    public static final int s0c_q0t = 39;

    public static final int s1fw_q0w = 41;
    public static final int s1lw_q0w = 42;
    public static final int s1fw_q0t = 43;
    public static final int s1lw_q0t = 44;
    public static final int s1c_q0w = 45;
    public static final int s1c_q0t = 46;

    // q0 and q1 's bigram features
    public static final int q0w_q1w = 50;
    public static final int q0t_q1w = 51;
    public static final int q0w_q1t = 52;
    public static final int q0t_q1t = 53;

    // trigram features
    public static final int s0c_s1c_q0t = 54;
    public static final int s0fw_s1c_q0t = 55;
    public static final int s0lw_s1c_q0t = 56;
    public static final int s0c_s1fw_q0t = 57;
    public static final int s0c_s1lw_q0t = 58;
    public static final int s0c_s1c_q0w = 59;

    // advanced features
    public static final int s0c_s0lc = 60;
    public static final int s1c_s1lc = 61;
    public static final int s0c_s0rc = 62;
    public static final int s1c_s1rc = 63;
    public static final int s0c_s0lc_s0rc = 64;
    public static final int s1c_s1lc_s1rc = 65;
    public static final int s0c_s0uc = 66;
    public static final int s1c_s1uc = 67;
}
