/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.corpus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nlp.nii.win.ConstantResource.Global;
import nlp.nii.win.corpus.stanford.syntax.Tree;
import nlp.nii.win.corpus.stanford.syntax.Trees;

/**
 *
 * @author lelightwin
 */
public class WinTreeBankReader {

    private final Trees.StandardTreeNormalizer normalizer = new Trees.StandardTreeNormalizer();
    /*-------------------------------------------------------------------------------*/
    /* for converting */
    /*-------------------------------------------------------------------------------*/
    /* for reading tree instance*/

    /**
     *
     * @param i
     * @return list of trees from section i
     */
    public List<Tree<String>> readSection(int i) {
        String index = "" + i;
        if (i < 10) {
            index = "0" + i;
        }

        List<Tree<String>> trees = new ArrayList();
        File[] files = new File(Global.wsjDirectory + "/" + index).listFiles();

        for (File file : files) {
            trees.addAll(readTreesFrom(file));
        }
        return trees;
    }

    public List<Tree<String>> readTreesFrom(File file) {
//        System.out.println(file.getAbsolutePath());
        List<Tree<String>> trees = new ArrayList();
        try {
            Trees.PennTreeReader reader = new Trees.PennTreeReader(new InputStreamReader(new FileInputStream(file)));
            while (reader.hasNext()) {
                Tree<String> tree = reader.next().getChild(0);
                tree = normalizer.transformTree(tree);
                tree.removeDuplicateUnary();
                tree.removeUnaryChains();

//                tagTravelling(tree);
                trees.add(tree);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WinTreeBankReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trees;
    }

    /**
     *
     * @param i
     * @return list of trees from section i
     */
    public List<Tree<Integer>> readNumericSection(int i) {

        String index = "" + i;
        if (i < 10) {
            index = "0" + i;
        }

        List<Tree<Integer>> trees = new ArrayList();
        File[] files = new File(Global.wsjDirectory + "/" + index).listFiles();

        for (File file : files) {
            trees.addAll(readNumericTreesFrom(file));
        }
        System.out.println("loading section " + i + " done!!!");
        return trees;
    }

    public List<Tree<Integer>> readNumericTreesFrom(File file) {
        List<Tree<Integer>> trees = new ArrayList();
        try {
            Trees.PennTreeReader reader = new Trees.PennTreeReader(new InputStreamReader(new FileInputStream(file)));
            while (reader.hasNext()) {
                Tree<String> tree = reader.next().getChild(0);
                tree = normalizer.transformTree(tree);
                tree.removeDuplicateUnary();
                tree.removeUnaryChains();
                Binarizer.binarizing(tree);

                trees.add(Corpuses.numericTransform(tree));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WinTreeBankReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trees;
    }

    private void tagTravelling(Tree<String> t) {
        if (!t.isLeaf()) {
            if (t.getLabel().equals("PRT|ADVP")) {
                System.out.println("catch!!!");
            }

            for (Tree<String> c : t.getChildren()) {
                tagTravelling(c);
            }
        }
    }

    public static void main(String[] args) {
        WinTreeBankReader reader = new WinTreeBankReader();
        for (int i = 0; i < 25; i++) {
            for (Tree<String> t : reader.readSection(i)) {
//                reader.tagTravelling(t);
            }
        }
    }
}
