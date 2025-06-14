# Climbing Gym

### Autorzy: Zuzanna Jedynak i Jakub Prygiel

Projekt został zrealizowany z użyciem nierelacyjnej bazy MongoDB, a implementacje działania CRUD i HTTP zostały napisane w Javie z pomocą Spring Boot'a.

---

## Zamysł bazy danych

Baza danych składa się z trzech kolekcji: `users`, `time_slots` oraz `entry_types`. Wykorzystaliśmy właściwości dokumentowego formatu MongoDB i użyliśmy zagnieżdżeń aby utrzymać więcej informacji w pojedynczych tabelach.

#### `users`

```json
{
  "id": "u1",
  "firstname": "Harry",
  "lastname": "Potter",
  "entries": [
    {
      "type": "Regular",
      "prices": {
        "regular": 35.0,
        "reduced": 20.0
      },
      "deadline": "2025-07-20",
      "amount": 3,
      "entry_id": "e1"
    }
  ],
  "reservations": [
    {
      "reservationId": "r1",
      "date": "2025-07-01",
      "status": "A",
      "day_time": "morning",
      "people_amount": 3
    }
  ],
  "register_date": "2024-01-15"
}
```

Każdy użytkownik poza podstawowymi informacjami jak imię, nazwisko, posiada także dwie listy. Listę `entries` trzymającą wszystkie zakupione wejściówki oraz listę `reservations` trzymającą wszystkie wykonane przez użytkownika rezerwacje.

#### `time_slots`

```json
{
  "id": "ts_2025-07-01",
  "date": "2025-07-01",
  "details": {
    "morning": {
      "maxSlots": 20,
      "reservedSlots": 3
    },
    "noon": {
      "maxSlots": 30,
      "reservedSlots": 0
    },
    "evening": {
      "maxSlots": 20,
      "reservedSlots": 0
    }
  }
}
```

Rezerwacje są jedynie na konkretną porę dnia, stąd w `time_slots` przetrzymywane są dni z podziałem na ranek, południe i wieczór.

#### `entry_types`

```json
{
  "id": "684d6d6e91f03f5ae49b5260",
  "name": "Four entries",
  "prices": {
    "regular": 110.0,
    "reduced": 90.0
  },
  "uses": 4,
  "entry_type": "4_entries",
  "day_limit": 30
}
```

Kolekcja utrzymująca wszystkie możliwe do zakupienia wejściówki, trzyma informacje o cenach, ilości wejść, oraz ograniczeniu czasowym na wykorzystanie.

---

##
