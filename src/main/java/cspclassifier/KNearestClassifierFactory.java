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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory for kNN classifier. Since such classifiers need to iterate 
 * all samples in order to classify a object, they are very slow on large dataset.
 * @author Chan Chung Kwong, modified by S.Welcker 2019
 * @param <T> the type of the objects to be classified
 */
public class KNearestClassifierFactory<T> extends BagClassifierFactory<Classifier<Frequencies<T>>,DocumentVectorsModel<T>,T>{
	private TfIdfFormula tfIdfFormula=TfIdfFormula.STANDARD;
	private int k=1;
	/**
	 * Create a kNN classifier factory
	 */
	public KNearestClassifierFactory(){
	}
	/**
	 * Set TF-IDF formula
	 * @param tfIdfFormula TF-IDF formula
	 * @return this
	 */
	public KNearestClassifierFactory<T> setTfIdfFormula(TfIdfFormula tfIdfFormula){
		this.tfIdfFormula=tfIdfFormula;
		return this;
	}
	/**
	 * @return TF-IDF formula
	 */
	public TfIdfFormula getTfIdfFormula(){
		return tfIdfFormula;
	}
	/**
	 * Set k
	 * @param k the number of neighborhood considered
	 * @return this
	 */
	public KNearestClassifierFactory<T> setK(int k){
		this.k=k;
		return this;
	}
	/**
	 * @return the number of neighborhood considered
	 */
	public int getK(){
		return k;
	}
	@Override
	public Classifier<Frequencies<T>> createClassifier(DocumentVectorsModel<T> model){
		return new KNearestClassifier<>(model.getProfiles(),model.getTotalDocumentFrequencies(),
				model.getSampleCount(),tfIdfFormula,k);
	}
	@Override
	public DocumentVectorsModel<T> createModel(){
		return new DocumentVectorsModel<>();
	}
	private static class KNearestClassifier<T> implements Classifier<Frequencies<T>>{
		private final TfIdfFormula tfIdfFormula;
		private final Frequencies<T> documentFrequencies;
		private final long documentCount;
		private final int k;
		private final Map<Category,DocumentVectorsModel.VectorsProfile<T>> profiles;
		public KNearestClassifier(Map<Category,DocumentVectorsModel.VectorsProfile<T>> profiles,
				Frequencies<T> documentFrequencies,
				long documentCount,TfIdfFormula tfIdfFormula,int k){
			this.profiles=profiles;
			this.documentFrequencies=documentFrequencies;
			this.documentCount=documentCount;
			this.tfIdfFormula=tfIdfFormula;
			this.k=k;
		}
		@Override
		public List<ClassificationResult> getCandidates(Frequencies<T> unknown,int max){
			LimitedSortedList<Pair<Category,Double>> cands=new LimitedSortedList<>(k,(p1,p2)->Double.compare(p1.getValue(),p2.getValue()));
			profiles.entrySet().stream().forEach((e)->e.getValue().getDocumentVectors().forEach(
					(sample)->cands.add(new Pair<>(e.getKey(),calculateDistance(sample,unknown)))));
			Map<Category,Long> counts=cands.getElements().stream().collect(Collectors.groupingBy((e)->e.getKey(),Collectors.counting()));
			return counts.entrySet().stream().map((e)->new ClassificationResult((e.getValue()+0.0)/k,e.getKey())).sorted().collect(Collectors.toList());
		}
		private double calculateDistance(Frequencies<T> v1,Frequencies<T> v2){
			double dist=0;
			for(Map.Entry<T,Counter> e:v1.toMap().entrySet()){
				T token=e.getKey();
				double diff=getTfIdf(token,e.getValue().getCount())-getTfIdf(token,v2.getFrequency(token));
				dist+=diff*diff;
			}
			for(Map.Entry<T,Counter> e:v2.toMap().entrySet()){
				T token=e.getKey();
				if(!v1.toMap().containsKey(token)){
					double diff=getTfIdf(token,0)-getTfIdf(token,e.getValue().getCount());
					dist+=diff*diff;
				}
			}
			return dist;
		}
		private double getTfIdf(T token,long freq){
			return tfIdfFormula.calculate(freq,documentFrequencies.getFrequency(token),documentCount);
		}
	}
	@Override
	protected String getName(){
		return "kNN";
	}
}