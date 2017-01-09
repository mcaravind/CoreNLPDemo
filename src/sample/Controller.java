package sample;

import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreNLPProtos;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.*;

import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;

public class Controller {
    @FXML
    Tab tabSentenceSegmentation;

    @FXML
    Tab tabPOSTagging;

    @FXML
    Tab tabNamedEntityRecognition;

    @FXML
    TextArea txtAreaSENInput;

    @FXML
    Button btnSENGetSentences;

    @FXML
    ListView lvwSENSentences;


    public void initialize(){
        tabSentenceSegmentation.setTooltip(new Tooltip("Sentence Segmentation"));
        tabPOSTagging.setTooltip(new Tooltip("POS Tagging"));
        tabNamedEntityRecognition.setTooltip(new Tooltip("Named Entity Recognition"));
    }

    public void btnPress_SENGetSentences(ActionEvent ae){
        String textToSplit = txtAreaSENInput.getText();
        List<String> sentences = GetSentences(textToSplit);
        List<HBox> hboxes = new ArrayList<>();
        for(String sentence:sentences){
            HBox hBox = new HBox();
            TextArea ta = new TextArea();
            ta.setText(sentence);
            ta.setWrapText(true);
            ta.setEditable(false);
            ta.setPrefRowCount(2);
            ta.setPrefWidth(800);
            hBox.getChildren().add(ta);
            hboxes.add(hBox);
        }
        ObservableList<HBox> hboxList = FXCollections.
                observableArrayList(hboxes);
        lvwSENSentences.setItems(hboxList);
    }

    public List<String> GetSentences(String input){
        List<String> sentenceList = new ArrayList<>();
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = input; // Add your text here!

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence:sentences){
            List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
            String originalString = edu.stanford.nlp.ling.Sentence.listToOriginalTextString(labels);
            sentenceList.add(originalString);
        }
        return sentenceList;
    }

    public List<String> GetWords(Sentence sentence){
        List<String> wordList = sentence.words();
        return wordList;
    }

    public List<String> GetPOSTags(Sentence sentence){
        List<String> tags = sentence.posTags();
        return tags;
    }

    public List<String> GetLemmas(Sentence sentence){
        List<String> lemmas = sentence.lemmas();
        return lemmas;
    }

    public List<String> GetNERTags(Sentence sentence){
        List<String> ners = sentence.nerTags();
        return ners;
    }

    public void GetParse(Sentence sentence){
        Tree parse = sentence.parse();
        parse.indentedXMLPrint();
    }
}
