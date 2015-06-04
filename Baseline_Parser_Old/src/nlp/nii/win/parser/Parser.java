/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser;

import java.io.File;
import nlp.nii.win.parser.decoder.Decoder;
import nlp.nii.win.parser.decoder.BeamSearchDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nlp.nii.win.ConstantResource.Global;
import nlp.nii.win.ConstantResource.Labels;
import nlp.nii.win.MachineLearning.Perceptron;
import nlp.nii.win.corpus.Binarizer;
import nlp.nii.win.corpus.WinTreeBankReader;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.corpus.stanford.syntax.Trees;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Oracle;
import nlp.nii.win.parser.metrics.LabeledConstituentEval;
import static nlp.nii.win.parser.ParserPerceptronCommunicator.*;
import nlp.nii.win.parser.decoder.BestFirstSearchDecoder;

/**
 *
 * @author lelightwin
 */
public class Parser {

    private Decoder decoder;
    private final LabeledConstituentEval<String> eval = new LabeledConstituentEval<>(Collections.singleton("ROOT"), Labels.punctuation);
//    private final LabeledConstituentEval<String> eval = new LabeledConstituentEval<>(Collections.singleton("ROOT"), new HashSet());

    public Parser(Decoder decoder) {
        this.decoder = decoder;
    }

    public void train(ArrayList<Oracle> oracles, int numberOfIterations, String model) {
        learner = new Perceptron(maximumModelSize, Global.actionsNum, BaselineFeature.quantity, true);
        System.out.println("Start training...");
        int iterationIdx;
        int oracleIdx;
        boolean learn = true;
        float errorRate;
        int error;

        for (int i = 0; i < numberOfIterations; i++) {
            iterationIdx = i + 1;
            error = 0;
            System.out.println("------------------------------------------------------------------------------------------------------------");
            System.out.println("Iteration " + iterationIdx + "th:");
            System.out.println("------------------------------------------------------------------------------------------------------------");
            if (learn) {
                long start = System.currentTimeMillis();
                double precision = 0.0;
                double recall = 0.0;
                long decodingTime = 0;
                long updatingTime = 0;
                for (int j = 0; j < oracles.size(); j++) {
                    oracleIdx = j + 1;
                    Oracle oracle = oracles.get(j);
                    // decoding and update parameters
                    long start1 = System.currentTimeMillis();
                    decoder.trainDecoding(oracle);
                    long end1 = System.currentTimeMillis();
                    decodingTime += (end1 - start1);

                    long start2 = System.currentTimeMillis();
                    DPState predict = decoder.getPredict();
                    DPState gold = decoder.getGold();
                    int step = decoder.getStep();
                    if (predict != null) {
                        if (predict.isFinish()) {
                            Tree<String> t1 = predict.makeTree();
                            Tree<String> t2 = oracle.getTree();
                            Binarizer.debinarizing(t1);
                            Binarizer.debinarizing(t2);
                            eval.evaluate(t1, t2);
                            precision += eval.getPrecision();
                            recall += eval.getRecall();
                        }
                        if (!decoder.isPredictIsRight()) {
                            error += 1;
                            //update parameters
                            ArrayList<DPState> predictChain = predict.listStates();
                            ArrayList<DPState> goldenChain = gold.listStates();
                            update(goldenChain, predictChain);
                            learner.accumulate(); // increase C (for average perceptron)!
                        }
                    }
                    long end2 = System.currentTimeMillis();
                    updatingTime += (end2 - start2);
                }
                long end = System.currentTimeMillis();
                if (error == 0) {
                    learn = false;
                }
                System.out.println("");
                System.out.println("ITERATION COMPLETE------------------------------------------------------------");
                errorRate = error * 100f / oracles.size();
                System.out.printf("Parseval precision: %.3f \n", 100 * precision / oracles.size());
                System.out.printf("Parseval recall: %.3f \n", 100 * recall / oracles.size());
                System.out.println("full sentence error rate: " + errorRate + "%");
                System.out.println("number of error sentence: " + error);
                System.out.println("decoding speed(sentences/second): " + oracles.size() / ((end - start) / 1000f));
                System.out.println("model size: " + learner.modelSize());
                System.out.println("Total time: " + (end - start) / 1000f);
                System.out.println("Total decoding time: " + decodingTime / 1000f);
                System.out.println("Total updating time: " + updatingTime / 1000f);
                if (!(new File(Global.modelDir + model).exists())) {
                    new File(Global.modelDir + model).mkdir();
                }
                learner.saveModels(
                        Global.modelDir + model + "." + iterationIdx + "/matrix.dat",
                        Global.modelDir + model + "." + iterationIdx + "/weights.dat");
            }

        }
        System.out.println("Learning complete!!!!");
    }

    private Trees.EmptyNodeStripper stripper = new Trees.EmptyNodeStripper();

    public void evaluate(ArrayList<Oracle> oracles, String model) {

        learner = new Perceptron(maximumModelSize, Global.actionsNum, BaselineFeature.quantity, false);
        System.out.println("Loading model...");
        learner.saveModels(
                Global.modelDir + model + "/matrix.dat",
                Global.modelDir + model + "/weights.dat");
        System.out.println("Done!!!");
        System.out.println("model size :" + learner.modelSize());

        System.out.println("Start evaluation...");

        Map<Integer, List<Float>> result = new HashMap();

        long start = System.currentTimeMillis();
        double precision = 0.0;
        double recall = 0.0;
        double fscore = 0.0;
        for (int i = 0; i < oracles.size(); i++) {
            Oracle oracle = oracles.get(i);

            long subStart = System.nanoTime();
            decoder.performDecoding(oracle.start());
            DPState predict = decoder.getPredict();
            long subEnd = System.nanoTime();
            if (predict.isFinish()) {
                Tree<String> t1 = predict.makeTree();
                Tree<String> t2 = oracle.getTree();
                Binarizer.debinarizing(t1);
                Binarizer.debinarizing(t2);
                fscore += eval.evaluate(t1, t2);
                precision += eval.getPrecision();
                recall += eval.getRecall();
            } else {
//                System.out.println("Fail!");
            }

            int size = stripper.transformTree(oracle.getTree()).getPreTerminals().size();
            float time = (subEnd - subStart) / 1000000f;

            if (!result.containsKey(size)) {
                result.put(size, new ArrayList());
            }
            result.get(size).add(time);

        }

        long end = System.currentTimeMillis();
        float totalTime = (end - start) / 1000f;
//        System.out.println("------------------------------------------------------------------------------------------------------------");
        System.out.printf("P:%.3f - R:%.3f - F:%.3f \n", 100 * precision / oracles.size(), 100 * recall / oracles.size(), 100 * fscore / oracles.size());
//        System.out.println("time: " + totalTime + " seconds");
        System.out.println("parsing speed(sentences/second): " + (oracles.size() / totalTime));

        Object[] sizes = result.keySet().toArray();
        Arrays.sort(sizes);

        for (Object size : sizes) {
            List<Float> times = result.get(size);
            float avgTime = 0.0f;
            for (Float time : times) {
                avgTime += time;
            }
            avgTime /= times.size();
            System.out.println(avgTime);
        }
    }

    public static void main(String[] args) {
        Decoder beamDecoder = new BeamSearchDecoder(32, true);
        Decoder bfsDecoder = new BestFirstSearchDecoder();

        Parser parser = new Parser(beamDecoder);
        WinTreeBankReader reader = new WinTreeBankReader();
        ArrayList<Oracle> oracles = new ArrayList<>();

        System.out.println("begin loading corpus...");
        for (int i = 2; i <= 21; i++) {
//        int i = 22;
            for (Tree<String> t : reader.readSection(i, 5000)) {
                oracles.add(new Oracle(t));
            }
        }
        System.out.println("Done loading!!!");
        parser.train(oracles, 5, "beam16.BF");
//        for (int j = 0; j < 42; j++) {
//            int iter = j + 1;
//            System.out.println("----------------------------------------------------------------------");
//            System.out.println("Iteration "+ iter);
//            parser.evaluate(oracles, "beam16"+"."+iter);
//        }
//        parser.evaluate(oracles, "beam16.15");
    }
}
