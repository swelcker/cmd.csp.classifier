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

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Feature selector based on Tf-Idf
 * @author Chan Chung Kwong, modified by S.Welcker 2019
 * @param <M> type of model
 * @param <T> underlying type to be classified
 */
public class TfIdfFeatureSelector<M extends TokenFrequenciesModel<T>,T> implements FeatureSelector<M,T>{
	private final int count;
	private final TfIdfFormula formula;
	/**
	 * Create a feature selector
	 * @param count the number of features to be kept
	 */
	public TfIdfFeatureSelector(int count){
		this.count=count;
		this.formula=TfIdfFormula.STANDARD;
	}
	/**
	 * Create a feature selector
	 * @param count the number of features to be kept
	 * @param formula the Tf-Idf formula
	 */
	public TfIdfFeatureSelector(int count,TfIdfFormula formula){
		this.count=count;
		this.formula=formula;
	}
	@Override
	public Set<T> select(M model,Function<M,? extends Classifier<Frequencies<T>>> classifierSupplier){
		LimitedSortedList<Pair<T,Double>> list=new LimitedSortedList<>(count,(p1,p2)->Double.compare(p2.getValue(),p1.getValue()));
		Frequencies<T> documentFrequencies=model.getTotalDocumentFrequencies();
		Frequencies<T> tokenFrequencies=model.getTotalTokenFrequencies();
		long sampleCount=model.getSampleCount();
		documentFrequencies.toMap().forEach((token,docFreq)->list.add(new Pair<>(token,
				formula.calculate(tokenFrequencies.getFrequency(token),docFreq.getCount(),sampleCount))));
		return list.getElements().stream().map((p)->p.getKey()).collect(Collectors.toSet());
	}
	@Override
	public String toString(){
		return "[TfIdfFeatureSelector"+count+"]";
	}
}
