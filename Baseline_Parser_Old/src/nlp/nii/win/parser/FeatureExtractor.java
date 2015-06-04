/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser;

import nlp.nii.win.ConstantResource.Labels;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Feature;
import nlp.nii.win.parser.element.Features;

/**
 *
 * @author lelightwin
 */
public class FeatureExtractor {

    public static Feature[] baselineFeaturesFrom(DPState p) {
        Feature[] feats = new Feature[BaselineFeature.quantity];
        int s0c = p.s0c(), s0t = p.s0t(), s0w = p.s0w(), s1c = p.s1c(), s1t = p.s1t(), s1w = p.s1w();
        int s2c = p.s2c(), s2t = p.s2t(), s2w = p.s2w(), s3c = p.s3c(), s3t = p.s3t(), s3w = p.s3w();
        int q0t = p.q0t(), q1t = p.q1t(), q2t = p.q2t(), q3t = p.q3t();
        int q0w = p.q0w(), q1w = p.q1w(), q2w = p.q2w(), q3w = p.q3w();
        int s0lc = p.s0lc(), s0rc = p.s0rc(), s0uc = p.s0uc(), s0lw = p.s0lw(), s0rw = p.s0rw(), s0uw = p.s0uw();
        int s1lc = p.s1lc(), s1rc = p.s1rc(), s1uc = p.s1uc(), s1lw = p.s1lw(), s1rw = p.s1rw(), s1uw = p.s1uw();

        feats[BaselineFeature.s0c_s0t] = Features.from(s0c, s0t);
        feats[BaselineFeature.s0c_s0w] = Features.from(s0c, s0w);

        feats[BaselineFeature.s1c_s1t] = Features.from(s1c, s1t);
        feats[BaselineFeature.s1c_s1w] = Features.from(s1c, s1w);

        feats[BaselineFeature.s2c_s2t] = Features.from(s2c, s2t);
        feats[BaselineFeature.s2c_s2w] = Features.from(s2c, s2w);

        feats[BaselineFeature.s3c_s3t] = Features.from(s3c, s3t);
        feats[BaselineFeature.s3c_s3w] = Features.from(s3c, s3w);

        feats[BaselineFeature.q0t_q0w] = Features.from(q0t, q0w);
        feats[BaselineFeature.q1t_q1w] = Features.from(q1t, q1w);
        feats[BaselineFeature.q2t_q2w] = Features.from(q2t, q2w);
        feats[BaselineFeature.q3t_q3w] = Features.from(q3t, q3w);

        feats[BaselineFeature.s0lc_s0lw] = Features.from(s0lc, s0lw);
        feats[BaselineFeature.s0rc_s0rw] = Features.from(s0rc, s0rw);
        feats[BaselineFeature.s0uc_s0uw] = Features.from(s0uc, s0uw);

        feats[BaselineFeature.s1lc_s1lw] = Features.from(s1lc, s1lw);
        feats[BaselineFeature.s1rc_s1rw] = Features.from(s1rc, s1rw);
        feats[BaselineFeature.s1uc_s1uw] = Features.from(s1uc, s1uw);

        // bigram features
        feats[BaselineFeature.s1w_s0w] = Features.from(s1w, s0w);
        feats[BaselineFeature.s1w_s0c] = Features.from(s0c, s1w);
        feats[BaselineFeature.s1c_s0w] = Features.from(s1c, s0w);
        feats[BaselineFeature.s1c_s0c] = Features.from(s1c, s0c);

        feats[BaselineFeature.s0w_q0w] = Features.from(s0w, q0w);
        feats[BaselineFeature.s0w_q0t] = Features.from(q0t, s0w);
        feats[BaselineFeature.s0c_q0w] = Features.from(s0c, q0w);
        feats[BaselineFeature.s0c_q0t] = Features.from(s0c, q0t);

        feats[BaselineFeature.q0w_q1w] = Features.from(q0w, q1w);
        feats[BaselineFeature.q0w_q1t] = Features.from(q1t, q0w);
        feats[BaselineFeature.q0t_q1w] = Features.from(q0t, q1w);
        feats[BaselineFeature.q0t_q1t] = Features.from(q0t, q1t);

        feats[BaselineFeature.s1w_q0w] = Features.from(s1w, q0w);
        feats[BaselineFeature.s1w_q0t] = Features.from(q0t, s1w);
        feats[BaselineFeature.s1c_q0w] = Features.from(s1c, q0w);
        feats[BaselineFeature.s1c_q0t] = Features.from(s1c, q0t);

        // trigram features
        feats[BaselineFeature.s0c_s1c_s2c] = Features.from(s0c, s1c, s2c);
        feats[BaselineFeature.s0w_s1c_s2c] = Features.from(s1c, s2c, s0w);
        feats[BaselineFeature.s0c_s1w_s2c] = Features.from(s0c, s2c, s1w);
        feats[BaselineFeature.s0c_s1c_s2w] = Features.from(s0c, s1c, s2w);

        feats[BaselineFeature.s0c_s1c_q0t] = Features.from(s0c, s1c, q0t);
        feats[BaselineFeature.s0w_s1c_q0t] = Features.from(s1c, q0t, s0w);
        feats[BaselineFeature.s0c_s1w_q0t] = Features.from(s0c, q0t, s1w);
        feats[BaselineFeature.s0c_s1c_q0w] = Features.from(s0c, s1c, q0w);
        return feats;
    }

    public static Feature[] spanFeaturesFrom(DPState p) {
        Feature[] feats = new Feature[SpanFeature.quantity];
        for (int i = 0; i < feats.length; i++) {
            feats[i] = null;
        }
        int q0t = p.q0t(), q1t = p.q1t(), q2t = p.q2t(), q3t = p.q3t();
        int q0w = p.q0w(), q1w = p.q1w(), q2w = p.q2w(), q3w = p.q3w();
        int s0c = p.s0c(), s0ft = p.s0_ft(), s0fw = p.s0_fw(), s0lt = p.s0_lt(), s0lw = p.s0_lw(), s0len = p.s0_len();
        int s1c = p.s1c(), s1ft = p.s1_ft(), s1fw = p.s1_fw(), s1lt = p.s1_lt(), s1lw = p.s1_lw(), s1at = p.s1_at(), s1aw = p.s1_aw(), s1len = p.s1_len();
        int s0lc = p.s0lc(), s0rc = p.s0rc(), s0uc = p.s0uc();
        int s1lc = p.s1lc(), s1rc = p.s1rc(), s1uc = p.s1uc();
        
        // queue features
        feats[SpanFeature.q0w_q0t] = Features.from(q0w, q0t);
        feats[SpanFeature.q1w_q1t] = Features.from(q1w, q1t);
        feats[SpanFeature.q2w_q2t] = Features.from(q2w, q2t);
        feats[SpanFeature.q3w_q3t] = Features.from(q3w, q3t);

        // s0's features
        feats[SpanFeature.s0c_s0ft] = Features.from(s0c, s0ft);
        feats[SpanFeature.s0c_s0fw] = Features.from(s0c, s0fw);
        feats[SpanFeature.s0c_s0lt] = Features.from(s0c, s0lt);
        feats[SpanFeature.s0c_s0lw] = Features.from(s0c, s0lw);
//        feats[SpanFeature.s0c_s0ft_s0lt] = Features.from(s0c, s0ft, s0lt);
//        feats[SpanFeature.s0c_s0ft_s0lw] = Features.from(s0c, s0ft, s0lw);
//        feats[SpanFeature.s0c_s0fw_s0lt] = Features.from(s0c, s0fw, s0lt);
//        feats[SpanFeature.s0c_s0fw_s0lw] = Features.from(s0c, s0fw, s0lw);
        feats[SpanFeature.s0c_s0len] = Features.from(s0c, s0len);
//        feats[SpanFeature.s0c_s0lc] = Features.from(s0c, s0lc);
//        feats[SpanFeature.s0c_s0rc] = Features.from(s0c, s0rc);
//        feats[SpanFeature.s0c_s0lc_s0rc] = Features.from(s0c, s0lc, s0rc);
//        feats[SpanFeature.s0c_s0uc] = Features.from(s0c, s0uc);

        // s1's features
        feats[SpanFeature.s1c_s1ft] = Features.from(s1c, s1ft);
        feats[SpanFeature.s1c_s1fw] = Features.from(s1c, s1fw);
        feats[SpanFeature.s1c_s1lt] = Features.from(s1c, s1lt);
        feats[SpanFeature.s1c_s1lw] = Features.from(s1c, s1lw);
        feats[SpanFeature.s1c_s1at] = Features.from(s1c, s1at);
        feats[SpanFeature.s1c_s1aw] = Features.from(s1c, s1aw);
//            feats[SpanFeature.s1c_s1ft_s1lt] = Features.from(s1c, s1ft, s1lt);
//            feats[SpanFeature.s1c_s1ft_s1lw] = Features.from(s1c, s1ft, s1lw);
//            feats[SpanFeature.s1c_s1fw_s1lt] = Features.from(s1c, s1fw, s1lt);
//            feats[SpanFeature.s1c_s1fw_s1lw] = Features.from(s1c, s1fw, s1lw);
        feats[SpanFeature.s1c_s1len] = Features.from(s1c, s1len);
//        feats[SpanFeature.s1c_s1lc] = Features.from(s1c, s1lc);
//        feats[SpanFeature.s1c_s1rc] = Features.from(s1c, s1rc);
//        feats[SpanFeature.s1c_s1lc_s1rc] = Features.from(s1c, s1lc, s1rc);
//        feats[SpanFeature.s1c_s1uc] = Features.from(s1c, s1uc);

        // s0 and s1 's bigram features
        feats[SpanFeature.s1lw_s0fw] = Features.from(s1lw, s0fw);
        feats[SpanFeature.s1lw_s0ft] = Features.from(s1lw, s0ft);
        feats[SpanFeature.s1lt_s0fw] = Features.from(s1lt, s0fw);
        feats[SpanFeature.s1lt_s0ft] = Features.from(s1lt, s0ft);

        feats[SpanFeature.s1c_s0fw] = Features.from(s1c, s0fw);
        feats[SpanFeature.s1c_s0lw] = Features.from(s1c, s0lw);

        feats[SpanFeature.s1fw_s0c] = Features.from(s1fw, s0c);
        feats[SpanFeature.s1lw_s0c] = Features.from(s1lw, s0c);

        feats[SpanFeature.s0fw_q0w] = Features.from(s0fw, q0w);
        feats[SpanFeature.s0lw_q0w] = Features.from(s0lw, q0w);
        feats[SpanFeature.s0fw_q0t] = Features.from(s0fw, q0t);
        feats[SpanFeature.s0lw_q0t] = Features.from(s0lw, q0t);
        feats[SpanFeature.s0c_q0w] = Features.from(s0c, q0w);
        feats[SpanFeature.s0c_q0t] = Features.from(s0c, q0t);

        feats[SpanFeature.s1fw_q0w] = Features.from(s1fw, q0w);
        feats[SpanFeature.s1lw_q0w] = Features.from(s1lw, q0w);
        feats[SpanFeature.s1fw_q0t] = Features.from(s1fw, q0t);
        feats[SpanFeature.s1lw_q0t] = Features.from(s1lw, q0t);
        feats[SpanFeature.s1c_q0w] = Features.from(s1c, q0w);
        feats[SpanFeature.s1c_q0t] = Features.from(s1c, q0t);

        // q0 and q1 's bigram features
        feats[SpanFeature.q0w_q1w] = Features.from(q0w, q1w);
        feats[SpanFeature.q0t_q1w] = Features.from(q0t, q1w);
        feats[SpanFeature.q0w_q1t] = Features.from(q0w, q1t);
        feats[SpanFeature.q0t_q1t] = Features.from(q0t, q1t);

        // trigram features
        feats[SpanFeature.s0c_s1c_q0t] = Features.from(s0c, s1c, q0t);
        feats[SpanFeature.s0fw_s1c_q0t] = Features.from(s0fw, s1c, q0t);
        feats[SpanFeature.s0lw_s1c_q0t] = Features.from(s0lw, s1c, q0t);
        feats[SpanFeature.s0c_s1fw_q0t] = Features.from(s0c, s1fw, q0t);
        feats[SpanFeature.s0c_s1lw_q0t] = Features.from(s0c, s1lw, q0t);
        feats[SpanFeature.s0c_s1c_q0w] = Features.from(s0c, s1c, q0w);

        return feats;
    }
}
