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

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Data set
 * @author Chan Chung Kwong, modified by S.Welcker 2019
 * @param <T> type of the data to be classified
 */
public class DataSet<T>{
	private final Supplier<Stream<Sample<T>>> sampleSupplier;
	private final String name;
	/**
	 * Create a dataset
	 * @param sampleSupplier to be used to generate samples, the streams 
	 * returned should return the same set of elements in the same order
	 * @param name the name
	 */
	public DataSet(Supplier<Stream<Sample<T>>> sampleSupplier,String name){
		this.sampleSupplier=sampleSupplier;
		this.name=name;
	}
	/**
	 * @return samples
	 */
	public Stream<Sample<T>> getSamples(){
		return sampleSupplier.get();
	}
	/**
	 * @return the name of the dataset
	 */
	public String getName(){
		return name;
	}
}
