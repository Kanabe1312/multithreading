# Aplicatii finale - lectia 02

Trei aplicatii capstone pentru volatile & atomic. **Trebuie sa le faci pe
toate trei** inainte sa treci la lectia 03. Fiecare integreaza notiunile
din lectia 02 (`AtomicInteger`, `AtomicLong`, `AtomicBoolean`,
`AtomicReference`, `compareAndSet` / spin loop, `getAndIncrement`) intr-un
scenariu din lumea reala.

| # | Aplicatie | Scenariu |
|---|-----------|----------|
| 1 | [Licitatie online](L02-1-licitatie-online/README.md)   | 50 licitatori dau bid-uri paralel, castiga cel mai mare |
| 2 | [API rate limiter](L02-2-rate-limiter/README.md)       | 500 cereri paralel, doar 100 accepate, restul respinse |
| 3 | [Telemetrie IoT](L02-3-telemetrie-iot/README.md)       | 1000 senzori trimit citiri paralel, stats live |

Fiecare are propriul README cu cerinte (Done When). Cand le rezolvi, ca
la L01 poti face cate un repo separat (gen `alex-licitatie-online`),
sau le tii in folderele de mai sus.
