package com.example.note.observe;

import com.example.note.data.entity.NoteData;

/**
 * Интерфейс наблюдателя
 * Ссылка на объяснения патерна: https://refactoring.guru/ru/design-patterns/observer
 *
 * Наблюдатель — это поведенческий паттерн проектирования,
 * который создаёт механизм подписки,
 * позволяющий одним объектам следить и реагировать на события, происходящие в других объектах.
 */
public interface Observer {
    void updateNoteDate(NoteData noteData);
}
