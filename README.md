# 📘 CoralDuel Placeholder API – Leaderboard Support

Questo plugin supporta dei **placeholder personalizzati** per mostrare leaderboard PvP dinamiche direttamente nel gioco tramite PlaceholderAPI.

---
## 🔧 Sintassi
%duel_leaderboard_statistica_posizione_campo%
---

## 📌 Parametri

| Parametro     | Descrizione                                                     |
|---------------|-----------------------------------------------------------------|
| `<statistica>` | Tipo di statistica per ordinare la classifica:                |
|                | - `wins` → Vittorie                                            |
|                | - `losses` → Sconfitte                                         |
|                | - `ties` → Pareggi                                             |
|                | - `streak` → Serie di vittorie consecutive                     |
| `<posizione>`  | Posizione nella classifica *(es. 1 = primo, 2 = secondo, ...)* |
| `<campo>`      | Campo da mostrare:                                             |
|                | - `name` → Nome del giocatore                                  |
|                | - `value` → Valore della statistica                            |

---

## 🧪 Esempi

| Placeholder                         | Risultato atteso                                   |
|-------------------------------------|----------------------------------------------------|
| `%duel_leaderboard_wins_1_name%`    | Nome del giocatore con più vittorie                |
| `%duel_leaderboard_wins_1_value%`   | Numero di vittorie del primo classificato          |
| `%duel_leaderboard_streak_3_name%`  | Nome del giocatore con la terza miglior streak     |
| `%duel_leaderboard_losses_5_value%` | Sconfitte del giocatore al quinto posto            |

---

## 🔄 Aggiornamento

- I dati vengono recuperati in **tempo reale dal database**.
- Nessuna cache necessaria.
- Le leaderboard riflettono sempre lo stato più aggiornato dei dati PvP.

---

## ✅ Requisiti

- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- CoralDuel installato correttamente con un database configurato

---

Per ulteriori placeholder (es. statistiche del player attuale), contattaci o consulta la documentazione avanzata.
