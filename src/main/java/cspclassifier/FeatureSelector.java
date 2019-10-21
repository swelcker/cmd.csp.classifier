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

/**
 * Feature selector
 * @author Chan Chung Kwong, modified by S.Welcker 2019
 * @param <M> type of model
 * @param <T> underlying type of objects to be classified
 */
public interface FeatureSelector<M extends TokenFrequenciesModel<T>,T>{
	/**
	 * Select features
	 * @param model the model
	 * @param classifierSupplier function that create classifier from model
	 * @return selected features
	 */
	Set<T> select(M model, Function<M, ? extends Classifier<Frequencies<T>>> classifierSupplier);
}
