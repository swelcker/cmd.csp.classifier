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

import java.util.Objects;

/**
 * Category, i.e. class label in classification
 * @author Chan Chung Kwong, modified by S.Welcker 2019
 */
public class Category{
	private final String name;
	/**
	 * Create a category
	 * @param name the label of the category
	 */
	public Category(String name){
		this.name=name;
	}
	/**
	 * Get the label of the category
	 * @return the label of the category
	 */
	public String getName(){
		return name;
	}
	@Override
	public boolean equals(Object obj){
		return obj instanceof Category&&Objects.equals(((Category)obj).name,name);
	}
	@Override
	public int hashCode(){
		int hash=3;
		hash=83*hash+Objects.hashCode(this.name);
		return hash;
	}
	@Override
	public String toString(){
		return name;
	}
}
