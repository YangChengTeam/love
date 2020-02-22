package com.yc.verbaltalk.chat.bean;

/**
 * Created by mayn on 2019/5/14.
 */

public class OrdersInitBean {

    /**
     * charge_order_sn : 201905141500547476
     * params : {"appid":"2017110409723362","email":"3258186647@qq.com","notify_url":"http://zyl.wk2.com/notify/alipay/default","partnerid":"2088821411331682","privatekey":"MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCP6cHHXB3x6W9UwOz07KlmU/zSksiwF9TOp5Jv3HftnJvZTJSHlOsrnvAbVFtzBK0lNEfNV4TXmS8Qonq62MuHaTJ3VC1p6FQsFnUltE8SRfpmz6iBObH7wCMaoY89Fi511tun86xE0u9HhGli231N99grbuuDrR9Qt2FmrLE04bQh/wcGS3AnIwpPttxn760Fbam89kvj8GMLDHvWmn5QLn//dIKBqm01Qo4PJLbcWaZIEDYQSH5ls1EqgzaMYirC7Qj7IgWyCpRbWhVGD+HXBl1IXyw+WuhyOM2G+0WR1AMXShRI3AR+7QEv27RU5qhO+79YdsVi01OB4Cl9MS7DAgMBAAECggEAO+MCiHuE2o5Rjetar/Fr7PE2XEpIyT6hh/2jqnkMTwzErgB4LpOB6X0SXc0U4SApDTpcRs8MsMtGEp4KhIaC666TGaUl4NSVcmNGDJKj9O657N6tOFlR/3lNIl/ow3rfipoGfjWgkmNUv2YSlNjRpAhnJGvcBcedKHGnTpq+g0ppXChytOAwSXExozBl4oYlke64Y+cT5/OQEy+Rj4BIaJnKyCv3NOiPRXNQS9EqCRAh1o8pJ5jEDV4t6jeYIMDZeqG7eBUMazQQP0P5EHiC01cKzT0BrvpqUaVnFAsrXqw3o9HgcoVcJfSq1LyY8d0J151Rvf0myiGQxMfaajZ/AQKBgQDGkR6EbyRsvn5/06bam/hEMiOmsKI5etwe7Khv61xf1J2+U9TG8ErC1BBAmGCCX3alHk4GuR73nNAz46rd40W9A02NB6S1LUwcNIqUyvqvJw6qgT3ZTh+512z5wY+eNJN/IuOJwSaZ3htoy7bEU/IOTPM0MyM8l3kjOsLYg3/wcwKBgQC5ic0SMqXNBkECvwdJL1rVtnzBKjip9kNs+3mN6uzKqwFK7R5somwqz7Zqm+zG0+bh3FNsB3fREk84iFoDkwYavEnksSzrlol+p1WLQLgMgLcPB6OMpjlK4KMxoHPYO+nfC4QDd6iprVoceIAgAQysIyiFe4vPnbDlf5STQ+DEcQKBgDEhx0NNnN2rZPGMFUUSQqPdJCUin4FJfR3JwQOwYPL1UPK/G27+FgGPJ1ZUXczkgh7pGLVhKOBr1LiCJM0yZxcVsiKrOX3671IrTf7zGoQsTdyyjfMu+XqqazSBSGAE8loK7/It8Lcx1eZgQaDihIo1UBgibx2W/UpSR9P69bUxAoGAW7Y1CxjEAitkOUJKDK/+u4Mf+a+wILtdKuLHfBIzCB8tXWcGUfabdzIDXoFCsimOh/iLt+udG1hslmo37GphaxfFgujdZnqb7mIyk8ni8DMzyZciDeUgjtWpdV91w94hxaIAmGIumejZkRczZh5+sBwU7J5cRr0Q8vB0dbLoyNECgYBRWkEcKdhMWM2DqqLtT9LdTnW/Ngrt50BQF2eX9yolBpro07pZjgRKDS+CdCUZZ3rkEAKAxkuyRT1qI741Vao8F8h80QX/O2uzwSD+tMCwKEQ4wy9SkBDFg4rQGcpfAGt/uZQ4BhLIe5ltm2Q1GwXi2VlYopjA7WdYg9TOlvVH4A=="}
     */

    public String charge_order_sn;
    public ParamsBean params;

    public static class ParamsBean {
        /**
         * appid : wx97a8dad615ab0283
         * mch_id : 1533747111
         * nonce_str : BNi67FntkA27pXut
         * prepay_id : wx1609515358710879661836522728810580
         * result_code : SUCCESS
         * return_code : SUCCESS
         * return_msg : OK
         * sign : F24FB4BC954F699C33831F4A9CB60720
         * timestamp : 1557971513
         * trade_type : APP
         */

        public String appid;
        public String mch_id;
        public String nonce_str;
        public String prepay_id;
        public String result_code;
        public String return_code;
        public String return_msg;
        public String sign;
        public String timestamp;
        public String trade_type;


        /**
         * appid : 2017110409723362
         * info : app_id=2017110409723362&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A0.01%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22201905160941202501%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flove.bshu.com%2Fnotify%2Falipay%2Fdefault&sign_type=RSA2&timestamp=2019-05-16+09%3A05%3A20&version=1.0&format=JSON
         * notify_url : http://love.bshu.com/notify/alipay/default
         * timestamp : 1557970880
         */

        public String info;
        public String notify_url;

        @Override
        public String toString() {
            return "ParamsBean{" +
                    "appid='" + appid + '\'' +
                    ", info='" + info + '\'' +
                    ", notify_url='" + notify_url + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }

        /**
         * appid : 2017110409723362
         * email : 3258186647@qq.com
         * notify_url : http://zyl.wk2.com/notify/alipay/default
         * partnerid : 2088821411331682
         * privatekey : MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCP6cHHXB3x6W9UwOz07KlmU/zSksiwF9TOp5Jv3HftnJvZTJSHlOsrnvAbVFtzBK0lNEfNV4TXmS8Qonq62MuHaTJ3VC1p6FQsFnUltE8SRfpmz6iBObH7wCMaoY89Fi511tun86xE0u9HhGli231N99grbuuDrR9Qt2FmrLE04bQh/wcGS3AnIwpPttxn760Fbam89kvj8GMLDHvWmn5QLn//dIKBqm01Qo4PJLbcWaZIEDYQSH5ls1EqgzaMYirC7Qj7IgWyCpRbWhVGD+HXBl1IXyw+WuhyOM2G+0WR1AMXShRI3AR+7QEv27RU5qhO+79YdsVi01OB4Cl9MS7DAgMBAAECggEAO+MCiHuE2o5Rjetar/Fr7PE2XEpIyT6hh/2jqnkMTwzErgB4LpOB6X0SXc0U4SApDTpcRs8MsMtGEp4KhIaC666TGaUl4NSVcmNGDJKj9O657N6tOFlR/3lNIl/ow3rfipoGfjWgkmNUv2YSlNjRpAhnJGvcBcedKHGnTpq+g0ppXChytOAwSXExozBl4oYlke64Y+cT5/OQEy+Rj4BIaJnKyCv3NOiPRXNQS9EqCRAh1o8pJ5jEDV4t6jeYIMDZeqG7eBUMazQQP0P5EHiC01cKzT0BrvpqUaVnFAsrXqw3o9HgcoVcJfSq1LyY8d0J151Rvf0myiGQxMfaajZ/AQKBgQDGkR6EbyRsvn5/06bam/hEMiOmsKI5etwe7Khv61xf1J2+U9TG8ErC1BBAmGCCX3alHk4GuR73nNAz46rd40W9A02NB6S1LUwcNIqUyvqvJw6qgT3ZTh+512z5wY+eNJN/IuOJwSaZ3htoy7bEU/IOTPM0MyM8l3kjOsLYg3/wcwKBgQC5ic0SMqXNBkECvwdJL1rVtnzBKjip9kNs+3mN6uzKqwFK7R5somwqz7Zqm+zG0+bh3FNsB3fREk84iFoDkwYavEnksSzrlol+p1WLQLgMgLcPB6OMpjlK4KMxoHPYO+nfC4QDd6iprVoceIAgAQysIyiFe4vPnbDlf5STQ+DEcQKBgDEhx0NNnN2rZPGMFUUSQqPdJCUin4FJfR3JwQOwYPL1UPK/G27+FgGPJ1ZUXczkgh7pGLVhKOBr1LiCJM0yZxcVsiKrOX3671IrTf7zGoQsTdyyjfMu+XqqazSBSGAE8loK7/It8Lcx1eZgQaDihIo1UBgibx2W/UpSR9P69bUxAoGAW7Y1CxjEAitkOUJKDK/+u4Mf+a+wILtdKuLHfBIzCB8tXWcGUfabdzIDXoFCsimOh/iLt+udG1hslmo37GphaxfFgujdZnqb7mIyk8ni8DMzyZciDeUgjtWpdV91w94hxaIAmGIumejZkRczZh5+sBwU7J5cRr0Q8vB0dbLoyNECgYBRWkEcKdhMWM2DqqLtT9LdTnW/Ngrt50BQF2eX9yolBpro07pZjgRKDS+CdCUZZ3rkEAKAxkuyRT1qI741Vao8F8h80QX/O2uzwSD+tMCwKEQ4wy9SkBDFg4rQGcpfAGt/uZQ4BhLIe5ltm2Q1GwXi2VlYopjA7WdYg9TOlvVH4A==
         */

       /* public String appid;
        public String email;
        public String notify_url;
        public String partnerid;
        public String privatekey;
        @Override
        public String toString() {
            return "ParamsBean{" +
                    "appid='" + appid + '\'' +
                    ", email='" + email + '\'' +
                    ", notify_url='" + notify_url + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    ", privatekey='" + privatekey + '\'' +
                    '}';
        }*/
    }

    @Override
    public String toString() {
        return "OrdersInitBean{" +
                "charge_order_sn='" + charge_order_sn + '\'' +
                ", params=" + params +
                '}';
    }
}
