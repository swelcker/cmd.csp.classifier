/*
 * Copyright (C) 2018 Chan Chung Kwong changed by S.Welcker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cspclassifier;

import cspclassifier.util.*;
import cspclassifier.validator.*;

import java.text.BreakIterator;
import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Some methods that are useful for new comer
 * @author Chan Chung Kwong, modified by S.Welcker 2019
 */
public class Starter{
	/**
	 * Get a ClassifierFactory that is currently considered a good try
	 * @param locale locale of the text to be classified
	 * @return a ClassifierFactory
	 */
	public static ClassifierFactory<? extends Classifier<String>,? extends Trainable<String>,String> getDefaultClassifierFactory(Locale locale){
		return getDefaultClassifierFactory(locale,false);
	}
	/**
	 * Get a ClassifierFactory that is currently considered a good try
	 * @param locale locale of the text to be classified
	 * @param stemming apply stemmer or not
	 * @return a ClassifierFactory
	 */
	public static ClassifierFactory<? extends Classifier<String>,? extends Trainable<String>,String> getDefaultClassifierFactory(Locale locale,boolean stemming){
		SvmClassifierFactory<String> svmClassifierFactory=new SvmClassifierFactory<>();
		return getDefaultClassifierFactory(locale,stemming,svmClassifierFactory);
	}
	/**
	 * Get a ClassifierFactory that is currently considered a good try
	 * @param locale locale of the text to be classified
	 * @param stemming apply stemmer or not
	 * @param base ClassifierFactory for Stream of string
	 * @return a ClassifierFactory
	 */
	public static ClassifierFactory<? extends Classifier<String>,? extends Trainable<String>,String> getDefaultClassifierFactory(Locale locale,boolean stemming,ClassifierFactory<Classifier<Frequencies<String>>,?,Frequencies<String>> base){
		return new PreprocessClassifierFactory<>(getDefaultPreprocessor(locale,stemming),base);
	}
	/**
	 * Get a text preprocessor that is currently considered a good try
	 * @param locale locale of the text to be classified
	 * @param stemming apply stemmer or not
	 * @return the text preprocessor
	 */
	public static Function<String,Frequencies<String>> getDefaultPreprocessor(Locale locale,boolean stemming){
		Function<String,String> preTokenize=TextPreprocessors.getJavaNormalizier(Normalizer.Form.NFKC);
		Function<String,Stream<String>> tokenizer;
		Function<Stream<String>,Stream<String>> postTokenize=TextPreprocessors.getWhitespaceFilter().andThen(TextPreprocessors.getFoldcaser());
		if(stemming)
			postTokenize=postTokenize.andThen(TextPreprocessors.getStemmer(locale));
		if(Locale.CHINESE.getISO3Language().equals(locale.getISO3Language())
				||Locale.JAPANESE.getISO3Language().equals(locale.getISO3Language())){
			tokenizer=TextPreprocessors.getJavaTokenizer(BreakIterator.getCharacterInstance(locale));
			postTokenize=postTokenize.andThen(TextPreprocessors.getNgramGenerator(2));
			if(Locale.CHINESE.getISO3Language().equals(locale.getISO3Language()))
				preTokenize=preTokenize.andThen(TextPreprocessors.getIcuTransformer("Traditional-Simplified"));
		}else{
			tokenizer=TextPreprocessors.getJavaTokenizer(BreakIterator.getWordInstance(locale));
		}
		return TextPreprocessors.of(preTokenize,tokenizer,postTokenize);
	}
	/**
	 * Get a ClassifierFactory based on a dataset
	 * @param locale locale of the text to be classified
	 * @param set dataset
	 * @return a ClassifierFactory
	 */
	public static ClassifierFactory<? extends Classifier<String>,? extends Trainable<String>,String> getDefaultClassifierFactory(Locale locale,DataSet<String> set){
		SplitDataSet<String> randomSplit=DataDivider.randomSplit(set,0.7);
		Validator<String> validator=new Validator<>();
		validator.validate(new SplitDataSet[]{randomSplit},getStandardClassifierFactories(locale));
		return validator.selectMostAccurate();
	}
	private static ClassifierFactory[] getStandardClassifierFactories(Locale locale){
		ClassifierFactory factory1=Starter.getDefaultClassifierFactory(locale,false,new TfIdfClassifierFactory());
		ClassifierFactory factory2=Starter.getDefaultClassifierFactory(locale,false,new BayesianClassifierFactory());
		//ClassifierFactory factory3=Starter.getDefaultClassifierFactory(locale,false,new KNearestClassifierFactory().setK(3));
		ClassifierFactory factory3=Starter.getDefaultClassifierFactory(locale,false,new C45ClassifierFactory());
		ClassifierFactory factory4=Starter.getDefaultClassifierFactory(locale,false,new SvmClassifierFactory());
		//ClassifierFactory factory5=Starter.getDefaultClassifierFactory(locale,true,new TfIdfClassifierFactory());
		//ClassifierFactory factory6=Starter.getDefaultClassifierFactory(locale,true,new BayesianClassifierFactory());
		//ClassifierFactory factory7=Starter.getDefaultClassifierFactory(locale,true,new KNearestClassifierFactory().setK(3));
		//ClassifierFactory factory8=Starter.getDefaultClassifierFactory(locale,true,new SvmClassifierFactory());
		return new ClassifierFactory[]{factory1,factory2,factory3,factory4};
	}
}
