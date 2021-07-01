package com.example.note.observe;


import com.example.note.data.NoteData;

import java.util.ArrayList;
import java.util.List;

/**
 * Паттерн "Наблюдатель"
 *
 * Класс Издатель
 */
public class Publisher {
    // Все подписчики
    private final List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    // Подписать
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    // Отписать
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    // Разослать событие
    public void notifySingle(NoteData cardData) {
        for (Observer observer : observers) {
            observer.updateNoteDate(cardData);
            unsubscribe(observer);
        }
    }
}
