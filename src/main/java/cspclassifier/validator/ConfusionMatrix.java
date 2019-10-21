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
package cspclassifier.validator;

import cspclassifier.*;
import cspclassifier.util.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Confusion matrix
 * @author Chan Chung Kwong, modified by S.Welcker 2019
 */
public class ConfusionMatrix{
	private final Frequencies<Pair<Category,Category>> matrix;
	private long testTime;
	/**
	 * Create a confusion matrix
	 */
	public ConfusionMatrix(){
		this.matrix=new Frequencies<>(true);
	}
	/**
	 * Advance a cell in the matrix by one
	 * @param real the actual category of a sample
	 * @param classified the classified category of a sample
	 */
	public void advanceFrequency(Category real,Category classified){
		matrix.advanceFrequency(new Pair<>(real,classified));
	}
	/**
	 * Advance a cell in the matrix by a given value
	 * @param real the actual category of a sample
	 * @param classified the classified category of a sample
	 * @param times to be added
	 */
	public void advanceFrequency(Category real,Category classified,long times){
		matrix.advanceFrequency(new Pair<>(real,classified),times);
	}
	/**
	 * @param real the actual category of a sample
	 * @param classified the classified category of a sample
	 * @return the number of samples in a category being classified into another category
	 */
	public long getFrequency(Category real,Category classified){
		return matrix.getFrequency(new Pair<>(real,classified));
	}
	/**
	 * Advance total time(millisecond) used for test
	 * @param testTime to be added
	 */
	public void advanceTestTime(long testTime){
		this.testTime=testTime;
	}
	/**
	 * @return total time being used for test
	 */
	public long getTestTime(){
		return testTime;
	}
	/**
	 * The F1 measure for a category
	 * @param category the category
	 * @return 2*recall*precision/(recall+precision)
	 */
	public double getF1Measure(Category category){
		double recall=getRecall(category);
		double precision=getPrecision(category);
		return 2*recall*precision/(recall+precision);
	}
	/**
	 * The recall rate for a category
	 * @param category the category
	 * @return the number of test samples correctly classified into the category
	 * divided by the number of samples in the category
	 */
	public double getRecall(Category category){
		Counter total=new Counter();
		matrix.toMap().forEach((k,v)->{
			if(Objects.equals(category,k.getKey()))
				total.advance(v.getCount());
		});
		return (matrix.getFrequency(new Pair<>(category,category))+0.0)/total.getCount();
	}
	/**
	 * The precision rate for a category
	 * @param category the category
	 * @return the number of test samples correctly classified into the category
	 * divided by the number of samples classified into the category
	 */
	public double getPrecision(Category category){
		Counter total=new Counter();
		matrix.toMap().forEach((k,v)->{
			if(Objects.equals(category,k.getValue()))
				total.advance(v.getCount());
		});
		return (matrix.getFrequency(new Pair<>(category,category))+0.0)/total.getCount();
	}
	/**
	 * @return the number of test samples correctly classified divided by the number of samples
	 */
	public double getAccuracy(){
		Counter accurate=new Counter();
		Counter total=new Counter();
		matrix.toMap().forEach((k,v)->{
			total.advance(v.getCount());
			if(Objects.equals(k.getKey(),k.getValue()))
				accurate.advance(v.getCount());
		});
		return (accurate.getCount()+0.0)/total.getCount();
	}
	/**
	 * @return the number of test samples
	 */
	public long getTestSampleCount(){
		Counter total=new Counter();
		matrix.toMap().forEach((k,v)->{
			total.advance(v.getCount());
		});
		return total.getCount();
	}
	/**
	 * @return Set of categories appeared in the matrix
	 */
	public Set<Category> getCategories(){
		HashSet<Category> categories=new HashSet<>();
		matrix.toMap().forEach((k,v)->{
			categories.add(k.getKey());
			categories.add(k.getValue());
		});
		return categories;
	}
	@Override
	public boolean equals(Object obj){
		return obj instanceof ConfusionMatrix&&Objects.equals(matrix,((ConfusionMatrix)obj).matrix);
	}
	@Override
	public int hashCode(){
		int hash=7;
		hash=79*hash+Objects.hashCode(this.matrix);
		return hash;
	}
	@Override
	public String toString(){
		Category[] categories=getCategories().toArray(new Category[0]);
		StringBuilder buf=new StringBuilder("\n");
		for(Category category:categories){
			buf.append('\t').append(category);
		}
		for(Category first:categories){
			buf.append('\n').append(first);
			for(Category second:categories){
				buf.append('\t').append(matrix.getFrequency(new Pair<>(first,second)));
			}
		}
		buf.append("\nSample:").append(getTestSampleCount());
		buf.append("\nAccuracy:").append(getAccuracy());
		buf.append("\nTime:").append(getTestTime());
		return buf.toString();
	}
}