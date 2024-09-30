package com.candlelightapps.stocknroll_backend.model.spoonacular.recipeByCriteria;

import java.util.ArrayList;

public record Data(ArrayList<Result>results, int offset, int number, int totalResults) {
}
