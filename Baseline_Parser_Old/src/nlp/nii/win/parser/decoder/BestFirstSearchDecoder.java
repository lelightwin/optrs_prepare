/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.decoder;

import java.util.ArrayList;
import java.util.Collections;
import nlp.nii.win.parser.Agenda;
import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Oracle;

/**
 *
 * @author lelightwin
 */
public class BestFirstSearchDecoder extends Decoder {

    @Override
    public void trainDecoding(Oracle oracle) {

        int beam = (int)Math.round(oracle.getTree().getPreTerminals().size()*1.4);
        Agenda agenda = new Agenda();
        ArrayList<DPState> beamStates = new ArrayList<>();
        step = 0;
        DPState startState = oracle.start();
        predict = startState;

        startState.setGold(true);
        beamStates.add(startState); // init beamStates with initial state of oracle
        predictIsRight = true;

        while (!predict.isFinish()) {
            // perform early update
            if (goldExist(beamStates) == null) {
                return;
            }

            agenda.clear();
            int actionGold = oracle.getNextAction(step);
            for (DPState state : beamStates) { // perform the shift-reduce actions from all the states in beam
                agenda.addAll(state.takeShift(actionGold));
                agenda.addAll(state.takeBReduce(actionGold));
                agenda.addExceptNull(state.takeFinish());
            }

            // after then, choose the new beam-best states based on their cost
            beamStates.clear();
            Collections.sort(agenda);
            beamStates = agenda.prune(beam);

            // for non-early update case
            if (!beamStates.isEmpty()) {
                predict = beamStates.get(0);
                predictIsRight = predict.isGold();
                gold = goldExist(agenda);
                step += 1;
            } else {
                return;
            }
        }
    }

    @Override
    public void performDecoding(DPState state) {
        int beam = (int) Math.round(state.getQueueSize()*1.4);

        Agenda agenda = new Agenda();
        predict = state;
        ArrayList<DPState> beamStates = new ArrayList<>();
        beamStates.add(state); // init beamStates with initial state of oracle

        while (!predict.isFinish()) {
            agenda.clear();
            for (DPState s : beamStates) { // perform the shift-reduce actions from all the states in beam
                agenda.addAll(s.takeShift(-1));
                agenda.addAll(s.takeBReduce(-1));
                agenda.addExceptNull(s.takeFinish());
            }

            // after then, choose the new beam-best states based on their cost
            beamStates.clear();
            Collections.sort(agenda);
            beamStates = agenda.prune(beam);

            // for non-early update case
            if (!beamStates.isEmpty()) {
//                predict = max(beamStates);
                predict = beamStates.get(0);
            } else {
                return;
            }
        }
    }

    public static void main(String[] args) {
    }
}
