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

import java.io.File;
import java.util.function.Function;

/**
 * Model that can be saved to and loaded from file system
 * @author Chan Chung Kwong, modified by S.Welcker 2019
 * @param <T> the type that is needed to convert to string while saving the model 
 */
public interface Persistable<T>{
	/**
	 * Save the model to filesystem
	 * @param directory where the model will be saved to
	 * @param encoder encode token to String without tab and new line
	 */
	void save(File directory, Function<T, String> encoder);
	/**
	 * Load the model from filesystem
	 * @param directory where the model is saved to
	 * @param decoder decode String to token
	 */
	void load(File directory, Function<String, T> decoder);
}
