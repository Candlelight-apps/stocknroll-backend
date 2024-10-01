package com.candlelightapps.stocknroll_backend.model.spoonacular;
import java.util.ArrayList;

public record Data(ArrayList<Result>results, int offset, int number, int totalResults) {
}
