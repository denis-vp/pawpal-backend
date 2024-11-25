package org.pawpal.model;

public enum AnimalType {
  DOG,
  CAT,
  REPTILE,
  COW,
  BIRD,
  RABBIT,
  FISH;

  boolean constains(String type) {
    for (AnimalType animalType : AnimalType.values()) {
      if (animalType.name().equals(type)) {
        return true;
      }
    }
    return false;
  }
}
