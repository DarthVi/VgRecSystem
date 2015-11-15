;C:\Users\VitoVincenzo\IdeaProjects\videogameRS\src\clp\videogamesRS.clp
(defmodule MAIN (export ?ALL))

;;*****************
;;* INITIAL STATE *
;;*****************

(deftemplate MAIN::attribute
   (slot name)
   (slot value)
   (slot certainty (default 100.0)))

(defrule MAIN::start
  (declare (salience 10000))
  =>
  (set-fact-duplication TRUE)
  (focus CHOOSE-FEATURES VIDEOGAMES))

(defrule MAIN::combine-certainties
  (declare (salience 100)
           (auto-focus TRUE))
  ?rem1 <- (attribute (name ?rel) (value ?val) (certainty ?per1))
  ?rem2 <- (attribute (name ?rel) (value ?val) (certainty ?per2))
  (test (neq ?rem1 ?rem2))
  =>
  (retract ?rem1)
  (modify ?rem2 (certainty (/ (- (* 100 (+ ?per1 ?per2)) (* ?per1 ?per2)) 100))))


;;******************
;; The RULES module
;;******************

(defmodule RULES (import MAIN ?ALL) (export ?ALL))

(deftemplate RULES::rule
  (slot certainty (default 100.0))
  (multislot if)
  (multislot then))

(defrule RULES::throw-away-ands-in-antecedent
  ?f <- (rule (if and $?rest))
  =>
  (modify ?f (if ?rest)))

(defrule RULES::remove-is-condition-when-satisfied
  ?f <- (rule (certainty ?c1) 
              (if ?attribute is ?value $?rest))
  (attribute (name ?attribute) 
             (value ?value) 
             (certainty ?c2))
  =>
  (modify ?f (certainty (min ?c1 ?c2)) (if ?rest)))

(defrule RULES::remove-is-not-condition-when-satisfied
  ?f <- (rule (certainty ?c1) 
              (if ?attribute is-not ?value $?rest))
  (attribute (name ?attribute) (value ~?value) (certainty ?c2))
  =>
  (modify ?f (certainty (min ?c1 ?c2)) (if ?rest)))

  (defrule RULES::perform-rule-consequent-with-certainty
    ?f <- (rule (certainty ?c1) 
                (if) 
                (then ?attribute is ?value with certainty ?c2 $?rest))
    =>
    (modify ?f (then ?rest))
    (assert (attribute (name ?attribute) 
                       (value ?value)
                       (certainty (/ (* ?c1 ?c2) 100)))))

(defrule RULES::perform-rule-consequent-without-certainty
  ?f <- (rule (certainty ?c1)
              (if)
              (then ?attribute is ?value $?rest))
  (test (or (eq (length$ ?rest) 0)
            (neq (nth 1 ?rest) with)))
  =>
  (modify ?f (then ?rest))
  (assert (attribute (name ?attribute) (value ?value) (certainty ?c1))))

;;*******************************
;;* CHOOSE GAME FEATURES RULES *
;;*******************************

(defmodule CHOOSE-FEATURES (import RULES ?ALL)
                            (import MAIN ?ALL))

(defrule CHOOSE-FEATURES::startit => (focus RULES))

(deffacts videogame-rules

        ;regole per scegliere il genere appropriato
        (rule (if main-game-purpose is relax and patience is basso)
              (then best-genre is action with certainty 60))

        (rule (if main-game-purpose is relax and patience is basso)
              (then best-genre is avventura-grafica with certainty 60))

        (rule (if main-game-purpose is relax and patience is basso)
              (then best-genre is fps with certainty 40))

        (rule (if main-game-purpose is relax and patience is medio)
              (then best-genre is platformer with certainty 40))

        (rule (if main-game-purpose is relax and patience is medio)
              (then best-genre is punta-e-clicca with certainty 40))

        (rule (if main-game-purpose is relax and patience is medio)
              (then best-genre is turn-based-strategy with certainty 20))

        (rule (if main-game-purpose is relax and patience is alto)
              (then best-genre is puzzle-game with certainty 30))

        (rule (if main-game-purpose is relax and patience is alto)
              (then best-genre is rts with certainty 20))

        (rule (if main-game-purpose is relax and patience is alto)
              (then best-genre is picchiaduro with certainty 30))

        (rule (if main-game-purpose is relax and patience is alto)
              (then best-genre is gdr with certainty 20))

        (rule (if main-game-purpose is sfida)
              (then best-genre is rts with certainty 80))

        (rule (if main-game-purpose is sfida)
              (then best-genre is 4X with certainty 60))

        (rule (if main-game-purpose is sfida)
              (then best-genre is fps with certainty 30))

        (rule (if main-game-purpose is sfida)
              (then best-genre is puzzle-game with certainty 30))

        (rule (if attitude is intelligenza)
              (then best-genre is rts with certainty 90))

        (rule (if attitude is intelligenza)
              (then best-genre is 4X with certainty 80))

        (rule (if attitude is intelligenza)
              (then best-genre is turn-based-strategy with certainty 80))

        (rule (if attitude is intelligenza)
              (then best-genre is puzzle-game with certainty 70))

        (rule (if attitude is intelligenza)
              (then best-genre is interactive-fiction with certainty 40))

        (rule (if gameplay-style is lento)
              (then best-genre is turn-based-strategy with certainty 80))

        (rule (if gameplay-style is lento)
              (then best-genre is 4X with certainty 60))

        (rule (if gameplay-style is lento)
              (then best-genre is rts with certainty 40))

        (rule (if gameplay-style is lento)
              (then best-genre is avventura-grafica with certainty 60))

        (rule (if gameplay-style is lento)
              (then best-genre is puzzle-game with certainty 30))

        (rule (if gameplay-style is lento)
              (then best-genre is interactive-fiction with certainty 50))

        (rule (if gameplay-style is bilanciato)
              (then best-genre is turn-based-strategy with certainty 30))

        (rule (if gameplay-style is bilanciato)
              (then best-genre is 4X with certainty 30))

        (rule (if gameplay-style is bilanciato)
              (then best-genre is avventura-grafica with certainty 50))

        (rule (if gameplay-style is bilanciato)
              (then best-genre is rts with certainty 40))

        (rule (if gameplay-style is bilanciato)
              (then best-genre is fps with certainty 35))

        (rule (if gameplay-style is frenetico)
              (then best-genre is rts with certainty 80))

        (rule (if gameplay-style is frenetico)
              (then best-genre is turn-based-strategy with certainty 30))

        (rule (if gameplay-style is frenetico)
              (then best-genre is 4X with certainty 30))

        (rule (if gameplay-style is frenetico)
              (then best-genre is action with certainty 60))

        (rule (if gameplay-style is frenetico)
              (then best-genre is fps with certainty 55))

        ;favourite-genre is

        (rule (if favourite-genre is gdr)
              (then best-genre is gdr with certainty 90))

        (rule (if favourite-genre is fps)
              (then best-genre is fps with certainty 90))

        (rule (if favourite-genre is rts)
              (then best-genre is rts with certainty 90))

        (rule (if favourite-genre is 4X)
              (then best-genre is 4X with certainty 90))

        (rule (if favourite-genre is turn-based-strategy)
              (then best-genre is turn-based-strategy with certainty 90))

        (rule (if favourite-genre is platformer)
              (then best-genre is platformer with certainty 90))

        (rule (if favourite-genre is puzzle-game)
              (then best-genre is puzzle-game with certainty 90))

        (rule (if favourite-genre is action)
              (then best-genre is action with certainty 90))

        (rule (if favourite-genre is picchiaduro)
              (then best-genre is picchiaduro with certainty 90))

        (rule (if favourite-genre is punta-e-clicca)
              (then best-genre is punta-e-clicca with certainty 90))

        (rule (if favourite-genre is interactive-fiction)
              (then best-genre is interactive-fiction with certainty 90))

        (rule (if favourite-genre is avventura-grafica)
              (then best-genre is avventura-grafica with certainty 90))

        ;regole che valutano il peso di plot-quality e user-plot-feature nella selezione del genere migliore per l'utente

        (rule (if user-plot-feature is imprevedibilita)
              (then best-genre is avventura-grafica with certainty 70))

        (rule (if user-plot-feature is imprevedibilita)
              (then best-genre is interactive-fiction with certainty 40))

        (rule (if user-plot-feature is imprevedibilita)
              (then best-genre is punta-e-clicca with certainty 30))

        (rule (if user-plot-feature is imprevedibilita)
              (then best-genre is action with certainty 25))

        (rule (if user-plot-feature is linearita)
              (then best-genre is action with certainty 70))

        (rule (if user-plot-feature is linearita)
            (then best-genre is platformer with certainty 40))

        (rule (if user-plot-feature is linearita)
            (then best-genre is avventura-grafica with certainty 30))

        (rule (if user-plot-feature is paradossi)
              (then best-genre is puzzle-game with certainty 80))

        (rule (if user-plot-feature is paradossi)
              (then best-genre is avventura-grafica with certainty 40))

        (rule (if user-plot-feature is paradossi)
              (then best-genre is interactive-fiction with certainty 30))

        (rule (if user-plot-feature is paradossi)
              (then best-genre is fps with certainty 20))

        (rule (if user-plot-feature is coerenza)
              (then best-genre is fps with certainty 30))

        (rule (if user-plot-feature is coerenza)
              (then best-genre is punta-e-clicca with certainty 30))

        (rule (if user-plot-feature is coerenza)
              (then best-genre is avventura-grafica with certainty 30))

        (rule (if user-plot-feature is coerenza)
              (then best-genre is platformer with certainty 10))

        (rule (if user-plot-feature is non-linearita)
              (then best-genre is avventura-grafica with certainty 40))

        (rule (if user-plot-feature is non-linearita)
              (then best-genre is fps with certainty 20))

        (rule (if user-plot-feature is non-linearita)
              (then best-genre is action with certainty 10))

        (rule (if user-plot-feature is non-linearita)
              (then best-genre is punta-e-clicca with certainty 10))

        (rule (if plot-quality is irrilevante and patience is basso)
              (then best-genre is fps with certainty 60))

        (rule (if plot-quality is irrilevante and patience is basso)
              (then best-genre is action with certainty 50))

        (rule (if plot-quality is irrilevante and gaming-experience is basso)
              (then best-genre is action with certainty 50))

        (rule (if plot-quality is irrilevante and gaming-experience is basso)
              (then best-genre is platformer with certainty 50))

        (rule (if plot-quality is irrilevante and gaming-experience is alto)
              (then best-genre is rts with certainty 70))

        (rule (if plot-quality is irrilevante and gaming-experience is alto)
              (then best-genre is 4X with certainty 50))

        (rule (if plot-quality is irrilevante and gaming-experience is alto)
              (then best-genre is fps with certainty 40))

        (rule (if plot-quality is irrilevante and gaming-experience is medio)
              (then best-genre is fps with certainty 60))

        (rule (if plot-quality is irrilevante and gaming-experience is medio)
              (then best-genre is rts with certainty 50))

        (rule (if plot-quality is irrilevante and gaming-experience is medio)
              (then best-genre is action with certainty 50))

        (rule (if plot-quality is importante and gaming-experience is medio)
              (then best-genre is avventura-grafica with certainty 60))

        (rule (if plot-quality is importante and gaming-experience is medio)
              (then best-genre is interactive-fiction with certainty 30))

        (rule (if plot-quality is importante and gaming-experience is medio)
              (then best-genre is punta-e-clicca with certainty 40))

        (rule (if plot-quality is importante and gaming-experience is medio)
              (then best-genre is puzzle-game with certainty 20))

        (rule (if plot-quality is importante and gaming-experience is basso)
              (then best-genre is avventura-grafica with certainty 50))

        (rule (if plot-quality is importante and gaming-experience is basso)
              (then best-genre is fps with certainty 40))

        (rule (if plot-quality is importante and gaming-experience is basso)
              (then best-genre is action with certainty 40))

        (rule (if plot-quality is importante and gaming-experience is basso)
              (then best-genre is platformer with certainty 40))

        (rule (if plot-quality is irrilevante and patience is alto)
              (then best-genre is rts with certainty 80))

        (rule (if plot-quality is irrilevante and patience is alto)
              (then best-genre is puzzle-game with certainty 40))

        (rule (if plot-quality is irrilevante and patience is alto)
              (then best-genre is 4X with certainty 40))

        (rule (if plot-quality is irrilevante and patience is alto)
              (then best-genre is turn-based-strategy with certainty 40))

        (rule (if plot-quality is irrilevante and patience is medio)
              (then best-genre is rts with certainty 40))

        (rule (if plot-quality is irrilevante and patience is medio)
              (then best-genre is 4X with certainty 80))

        (rule (if plot-quality is irrilevante and patience is medio)
              (then best-genre is puzzle-game with certainty 60))

        (rule (if plot-quality is irrilevante and patience is medio)
              (then best-genre is turn-based-strategy with certainty 40))

        ;regole per scegliere il livello di difficoltà e la curva di apprendimento
        (rule (if gaming-experience is basso)
              (then best-difficulty is easy with certainty 95))

        (rule (if gaming-experience is basso)
              (then best-difficulty is medium with certainty 10))

        (rule (if gaming-experience is basso)
              (then best-difficulty is hard with certainty 5))

        (rule (if gaming-experience is basso)
              (then best-difficulty is user-choice with certainty 20))

        (rule (if gaming-experience is medio)
              (then best-difficulty is user-choice with certainty 70))

        (rule (if gaming-experience is medio)
              (then best-difficulty is medium with certainty 60))

        (rule (if gaming-experience is medio)
              (then best-difficulty is hard with certainty 30))

        (rule (if gaming-experience is medio)
              (then best-difficulty is easy with certainty 20))

        (rule (if gaming-experience is alto)
              (then best-difficulty is hard with certainty 90))

        (rule (if gaming-experience is alto)
              (then best-difficulty is user-choice with certainty 80))

        (rule (if gaming-experience is alto)
              (then best-difficulty is medium with certainty 40))

        (rule (if gaming-experience is alto)
              (then best-difficulty is easy with certainty 20))

        (rule (if favourite-genre is avventura-grafica)
              (then best-difficulty is easy with certainty 60))

        (rule (if patience is basso)
              (then best-learning-curve is quick-growth with certainty 95))

        (rule (if patience is basso)
              (then best-learning-curve is slow-growth with certainty 10))

        (rule (if patience is basso)
              (then best-learning-curve is s-curve with certainty 20))

        (rule (if patience is basso)
              (then best-learning-curve is linear with certainty 20))

        (rule (if patience is medio)
              (then best-learning-curve is linear with certainty 60))

        (rule (if patience is medio)
              (then best-learning-curve is s-curve with certainty 30))

        (rule (if patience is medio)
              (then best-learning-curve is quick-growth with certainty 20))

        (rule (if patience is medio)
              (then best-learning-curve is slow-growth with certainty 20))

        (rule (if patience is alto)
              (then best-learning-curve is slow-growth with certainty 50))

        (rule (if patience is alto)
              (then best-learning-curve is s-curve with certainty 40))

        (rule (if patience is alto)
              (then best-learning-curve is linear with certainty 20))

        (rule (if patience is alto)
              (then best-learning-curve is quick-growth with certainty 20))

        (rule (if user-learning-attitude is si)
              (then best-learning-curve is slow-growth with certainty 50))

        (rule (if user-learning-attitude is si)
              (then best-learning-curve is s-curve with certainty 50))

        (rule (if user-learning-attitude is si)
              (then best-learning-curve is quick-growth with certainty 10))

        (rule (if user-learning-attitude is si)
              (then best-learning-curve is linear with certainty 20))

        (rule (if user-learning-attitude is no)
              (then best-learning-curve is quick-growth with certainty 50))

        (rule (if user-learning-attitude is no)
              (then best-learning-curve is linear with certainty 50))

        (rule (if user-learning-attitude is no)
              (then best-learning-curve is slow-growth with certainty 10))

        (rule (if user-learning-attitude is no)
              (then best-learning-curve is s-curve with certainty 20))

        (rule (if user-learning-attitude is non-so)
              (then best-learning-curve is linear with certainty 80))

        (rule (if user-learning-attitude is non-so)
              (then best-learning-curve is s-curve with certainty 40))

        (rule (if user-learning-attitude is non-so)
              (then best-learning-curve is slow-growth with certainty 40))

        (rule (if user-learning-attitude is non-so)
              (then best-learning-curve is quick-growth with certainty 40))

        ;regole per scegliere la qualità di trama appropriata
        (rule (if main-game-purpose is relax and plot-quality is importante)
              (then best-plot is excellent with certainty 70))

        (rule (if main-game-purpose is relax and plot-quality is importante)
              (then best-plot is good with certainty 50))

        (rule (if main-game-purpose is relax and plot-quality is importante)
              (then best-plot is awful with certainty 10))

        (rule (if main-game-purpose is relax and plot-quality is importante)
              (then best-plot is normal with certainty 20))

        (rule (if main-game-purpose is relax and plot-quality is importante)
              (then best-plot is no-plot with certainty 10))

        (rule (if main-game-purpose is relax and plot-quality is irrilevante)
              (then best-plot is excellent with certainty 20))

        (rule (if main-game-purpose is relax and plot-quality is irrilevante)
              (then best-plot is no-plot with certainty 20))

        (rule (if main-game-purpose is relax and plot-quality is irrilevante)
              (then best-plot is awful with certainty 20))

        (rule (if main-game-purpose is relax and plot-quality is irrilevante)
              (then best-plot is good with certainty 20))

        (rule (if main-game-purpose is relax and plot-quality is irrilevante)
              (then best-plot is normal with certainty 20))

        (rule (if main-game-purpose is divertimento and plot-quality is irrilevante)
              (then best-plot is awful with certainty 10))

        (rule (if main-game-purpose is divertimento and plot-quality is irrilevante)
              (then best-plot is good with certainty 30))

        (rule (if main-game-purpose is divertimento and plot-quality is irrilevante)
              (then best-plot is normal with certainty 20))

        (rule (if main-game-purpose is divertimento and plot-quality is irrilevante)
              (then best-plot is excellent with certainty 20))

        (rule (if main-game-purpose is divertimento and plot-quality is irrilevante)
              (then best-plot is no-plot with certainty 20))

        (rule (if main-game-purpose is divertimento and plot-quality is importante)
              (then best-plot is normal with certainty 40))

        (rule (if main-game-purpose is divertimento and plot-quality is importante)
              (then best-plot is good with certainty 20))

        (rule (if main-game-purpose is divertimento and plot-quality is importante)
              (then best-plot is excellent with certainty 20))

        (rule (if main-game-purpose is divertimento and plot-quality is importante)
              (then best-plot is awful with certainty 10))

        (rule (if main-game-purpose is divertimento and plot-quality is importante)
              (then best-plot is no-plot with certainty 10))

        (rule (if main-game-purpose is sfida)
              (then best-plot is no-plot with certainty 40))

        (rule (if main-game-purpose is sfida)
              (then best-plot is good with certainty 40))

        (rule (if main-game-purpose is sfida)
              (then best-plot is excellent with certainty 20))

        (rule (if main-game-purpose is sfida)
              (then best-plot is awful with certainty 10))

        (rule (if main-game-purpose is sfida)
              (then best-plot is normal with certainty 20))

        (rule (if favourite-genre is avventura-grafica)
              (then best-plot is excellent with certainty 80))

        (rule (if favourite-genre is avventura-grafica)
              (then best-plot is good with certainty 10))

        (rule (if favourite-genre is avventura-grafica)
              (then best-plot is normal with certainty 5))

        (rule (if favourite-genre is avventura-grafica)
              (then best-plot is awful with certainty 5))

        (rule (if favourite-genre is avventura-grafica)
              (then best-plot is no-plot with certainty 5))

        (rule (if favourite-genre is interactive-fiction)
              (then best-plot is good with certainty 50))

        (rule (if favourite-genre is interactive-fiction)
              (then best-plot is excellent with certainty 50))

        (rule (if favourite-genre is interactive-fiction)
              (then best-plot is awful with certainty 10))

        (rule (if favourite-genre is interactive-fiction)
              (then best-plot is normal with certainty 20))

        (rule (if favourite-genre is interactive-fiction)
              (then best-plot is no-plot with certainty 10))

        (rule (if favourite-genre is action)
              (then best-plot is good with certainty 50))

        (rule (if favourite-genre is action)
              (then best-plot is normal with certainty 50))

        (rule (if favourite-genre is action)
              (then best-plot is excellent with certainty 30))

        (rule (if favourite-genre is action)
              (then best-plot is awful with certainty 20))

        (rule (if favourite-genre is action)
              (then best-plot is no-plot with certainty 10))

        (rule (if favourite-genre is gdr)
              (then best-plot is excellent with certainty 50))

        (rule (if favourite-genre is gdr)
              (then best-plot is good with certainty 50))

        (rule (if favourite-genre is gdr)
              (then best-plot is normal with certainty 20))

        (rule (if favourite-genre is gdr)
              (then best-plot is awful with certainty 20))

        (rule (if favourite-genre is gdr)
              (then best-plot is no-plot with certainty 10))        

        ;regole per scegliere la qualità audio
        (rule (if user-audio-quality is piu'-di-20)
              (then best-audio is excellent with certainty 70))

        (rule (if user-audio-quality is piu'-di-20)
              (then best-audio is good with certainty 30))

        (rule (if user-audio-quality is piu'-di-20)
              (then best-audio is normal with certainty 20))

        (rule (if user-audio-quality is piu'-di-20)
              (then best-audio is awful with certainty 10))

        (rule (if user-audio-quality is piu'-di-20)
              (then best-audio is no-audio with certainty 5))

        (rule (if user-audio-quality is fra-10-e-20)
              (then best-audio is excellent with certainty 20))

        (rule (if user-audio-quality is fra-10-e-20)
              (then best-audio is normal with certainty 40))

        (rule (if user-audio-quality is fra-10-e-20)
              (then best-audio is good with certainty 40))

        (rule (if user-audio-quality is fra-10-e-20)
              (then best-audio is awful with certainty 20))

        (rule (if user-audio-quality is fra-10-e-20)
              (then best-audio is no-audio with certainty 10))

        (rule (if user-audio-quality is meno-di-10)
              (then best-audio is no-audio with certainty 20))

        (rule (if user-audio-quality is meno-di-10)
              (then best-audio is awful with certainty 20))

        (rule (if user-audio-quality is meno-di-10)
              (then best-audio is normal with certainty 50))

        (rule (if user-audio-quality is meno-di-10)
              (then best-audio is good with certainty 30))

        (rule (if user-audio-quality is meno-di-10)
              (then best-audio is excellent with certainty 20))

        ;regole che influenzano la scelta sulla grafica

        (rule (if graphics-detail-quality is realismo)
              (then best-graphics is excellent with certainty 80))

        (rule (if graphics-detail-quality is realismo)
              (then best-graphics is good with certainty 20))

        (rule (if graphics-detail-quality is realismo)
              (then best-graphics is normal with certainty 10))

        (rule (if graphics-detail-quality is realismo)
              (then best-graphics is awful with certainty 10))

        (rule (if graphics-detail-quality is funzionale)
              (then best-graphics is awful with certainty 25))

        (rule (if graphics-detail-quality is funzionale)
              (then best-graphics is normal with certainty 30))

        (rule (if graphics-detail-quality is funzionale)
              (then best-graphics is good with certainty 30))

        (rule (if graphics-detail-quality is funzionale)
              (then best-graphics is excellent with certainty 25))

        (rule (if favourite-genre is avventura-grafica)
              (then best-graphics is excellent with certainty 70))

        (rule (if favourite-genre is avventura-grafica)
              (then best-graphics is good with certainty 30))

        (rule (if favourite-genre is avventura-grafica)
              (then best-graphics is normal with certainty 20))

        (rule (if favourite-genre is avventura-grafica)
              (then best-graphics is awful with certainty 10))

        (rule (if favourite-genre is gdr)
              (then best-graphics is excellent with certainty 60))

        (rule (if favourite-genre is gdr)
              (then best-graphics is good with certainty 40))

        (rule (if favourite-genre is gdr)
              (then best-graphics is normal with certainty 30))

        (rule (if favourite-genre is gdr)
              (then best-graphics is awful with certainty 10))

        (rule (if favourite-genre is interactive-fiction)
              (then best-graphics is awful with certainty 25))

        (rule (if favourite-genre is interactive-fiction)
              (then best-graphics is normal with certainty 25))

        (rule (if favourite-genre is interactive-fiction)
              (then best-graphics is good with certainty 25))

        (rule (if favourite-genre is interactive-fiction)
              (then best-graphics is excellent with certainty 25))

        (rule (if favourite-genre is rts)
              (then best-graphics is awful with certainty 25))

        (rule (if favourite-genre is rts)
              (then best-graphics is normal with certainty 25))

        (rule (if favourite-genre is rts)
              (then best-graphics is good with certainty 25))

        (rule (if favourite-genre is rts)
              (then best-graphics is excellent with certainty 25))

        (rule (if favourite-genre is 4X)
              (then best-graphics is awful with certainty 25))

        (rule (if favourite-genre is 4X)
              (then best-graphics is normal with certainty 25))

        (rule (if favourite-genre is 4X)
              (then best-graphics is good with certainty 25))

        (rule (if favourite-genre is 4X)
              (then best-graphics is excellent with certainty 25))

        (rule (if gaming-experience is alto)
              (then best-graphics is awful with certainty 25))

        (rule (if gaming-experience is alto)
              (then best-graphics is awful with certainty 25))

        (rule (if gaming-experience is alto)
              (then best-graphics is normal with certainty 25))

        (rule (if gaming-experience is alto)
              (then best-graphics is good with certainty 25))

        (rule (if gaming-experience is alto)
              (then best-graphics is excellent with certainty 25))

        (rule (if gaming-experience is basso)
              (then best-graphics is awful with certainty 10))

        (rule (if gaming-experience is basso)
              (then best-graphics is normal with certainty 10))

        (rule (if gaming-experience is basso)
              (then best-graphics is good with certainty 30))

        (rule (if gaming-experience is basso)
              (then best-graphics is excellent with certainty 50))

        (rule (if gaming-experience is medio)
              (then best-graphics is awful with certainty 10))

        (rule (if gaming-experience is medio)
              (then best-graphics is normal with certainty 20))

        (rule (if gaming-experience is medio)
              (then best-graphics is good with certainty 30))

        (rule (if gaming-experience is medio)
              (then best-graphics is excellent with certainty 40))
        
        ;regole che influenzano la scelta del tipo di AI (reattività dei nemici, ostacoli da affrontare per raggiungere gli obiettivi di gioco, ecc)

        (rule (if ai-implementation is si)
              (then best-AI is challenging with certainty 50))

        (rule (if ai-implementation is si)
              (then best-AI is balanced with certainty 50))

        (rule (if ai-implementation is si)
              (then best-AI is tanked with certainty 20))

        (rule (if ai-implementation is si)
              (then best-AI is dumb with certainty 10))

        (rule (if ai-implementation is no)
              (then best-AI is balanced with certainty 50))

        (rule (if ai-implementation is no)
              (then best-AI is dumb with certainty 20))

        (rule (if ai-implementation is no)
              (then best-AI is tanked with certainty 10))

        (rule (if ai-implementation is no)
              (then best-AI is challenging with certainty 20))

        (rule (if ai-implementation is non-so)
              (then best-AI is balanced with certainty 25))

        (rule (if ai-implementation is non-so)
              (then best-AI is tanked with certainty 25))

        (rule (if ai-implementation is non-so)
              (then best-AI is dumb with certainty 25))

        (rule (if ai-implementation is non-so)
              (then best-AI is challenging with certainty 25))

        (rule (if favourite-genre is rts)
              (then best-AI is challenging with certainty 70))

        (rule (if favourite-genre is rts)
              (then best-AI is balanced with certainty 30))

        (rule (if favourite-genre is rts)
              (then best-AI is tanked with certainty 20))

        (rule (if favourite-genre is rts)
              (then best-AI is dumb with certainty 10))

        (rule (if favourite-genre is 4X)
              (then best-AI is challenging with certainty 70))

        (rule (if favourite-genre is 4X)
              (then best-AI is balanced with certainty 30))

        (rule (if favourite-genre is 4X)
              (then best-AI is tanked with certainty 20))

        (rule (if favourite-genre is 4X)
              (then best-AI is dumb with certainty 10))

        (rule (if favourite-genre is fps)
              (then best-AI is challenging with certainty 60))

        (rule (if favourite-genre is fps)
              (then best-AI is balanced with certainty 40))

        (rule (if favourite-genre is fps)
              (then best-AI is tanked with certainty 20))

        (rule (if favourite-genre is fps)
              (then best-AI is dumb with certainty 10))

        (rule (if main-game-purpose is sfida)
              (then best-AI is challenging with certainty 70))

        (rule (if main-game-purpose is sfida)
              (then best-AI is tanked with certainty 20))

        (rule (if main-game-purpose is sfida)
              (then best-AI is balanced with certainty 20))

        (rule (if main-game-purpose is sfida)
              (then best-AI is dumb with certainty 10))

        (rule (if main-game-purpose is divertimento)
              (then best-AI is balanced with certainty 50))

        (rule (if main-game-purpose is divertimento)
              (then best-AI is dumb with certainty 20))

        (rule (if main-game-purpose is divertimento)
              (then best-AI is challenging with certainty 30))

        (rule (if main-game-purpose is divertimento)
              (then best-AI is tanked with certainty 20))

        (rule (if main-game-purpose is relax)
              (then best-AI is dumb with certainty 70))

        (rule (if main-game-purpose is relax)
              (then best-AI is balanced with certainty 30))

        (rule (if main-game-purpose is relax)
              (then best-AI is challenging with certainty 10))

        (rule (if main-game-purpose is relax)
              (then best-AI is tanked with certainty 10))

        (rule (if attitude is intelligenza)
              (then best-AI is challenging with certainty 70))

        (rule (if attitude is intelligenza)
              (then best-AI is balanced with certainty 30))

        (rule (if attitude is intelligenza)
              (then best-AI is tanked with certainty 10))

        (rule (if attitude is intelligenza)
              (then best-AI is dumb with certainty 5))

        (rule (if attitude is furbizia)
              (then best-AI is challenging with certainty 50))

        (rule (if attitude is furbizia)
              (then best-AI is balanced with certainty 40))

        (rule (if attitude is furbizia)
              (then best-AI is tanked with certainty 20))

        (rule (if attitude is furbizia)
              (then best-AI is dumb with certainty 5))

        (rule (if attitude is mix)
              (then best-AI is challenging with certainty 20))

        (rule (if attitude is mix)
              (then best-AI is balanced with certainty 60))

        (rule (if attitude is mix)
              (then best-AI is tanked with certainty 10))

        (rule (if attitude is mix)
              (then best-AI is dumb with certainty 10))

        (rule (if attitude is forza-bruta)
              (then best-AI is challenging with certainty 20))

        (rule (if attitude is forza-bruta)
              (then best-AI is balanced with certainty 60))

        (rule (if attitude is forza-bruta)
              (then best-AI is dumb with certainty 20))

        (rule (if attitude is forza-bruta)
              (then best-AI is tanked with certainty 20))

        (rule (if patience is basso)
              (then best-AI is dumb with certainty 40))

        (rule (if patience is basso)
              (then best-AI is balanced with certainty 60))

        (rule (if patience is basso)
              (then best-AI is tanked with certainty 10))

        (rule (if patience is basso)
              (then best-AI is challenging with certainty 20))

        (rule (if patience is medio)
              (then best-AI is balanced with certainty 50))

        (rule (if patience is medio)
              (then best-AI is dumb with certainty 20))

        (rule (if patience is medio)
              (then best-AI is challenging with certainty 30))

        (rule (if patience is medio)
              (then best-AI is tanked with certainty 20))

        (rule (if patience is alto)
              (then best-AI is balanced with certainty 50))

        (rule (if patience is alto)
              (then best-AI is dumb with certainty 10))

        (rule (if patience is alto)
              (then best-AI is challenging with certainty 40))

        (rule (if patience is alto)
              (then best-AI is tanked with certainty 20))

        ;regole per il miglior world building
        (rule (if favourite-world-build-expert is open-world)
              (then best-world-design is open-world with certainty 80))

        (rule (if favourite-world-build-expert is open-world)
              (then best-world-design is closed-world with certainty 20))

        (rule (if favourite-world-build-expert is closed-world)
              (then best-world-design is closed-world with certainty 80))

        (rule (if favourite-world-build-expert is closed-world)
              (then best-world-design is open-world with certainty 20))

        (rule (if favourite-world-build-expert is indifferente)
              (then best-world-design is open-world with certainty 40))

        (rule (if favourite-world-build-expert is indifferente)
              (then best-world-design is closed-world with certainty 40))

        (rule (if favourite-world-build-expert is indifferente)
              (then best-world-design is indifferente with certainty 60))

        (rule (if favourite-world-build-novice is si)
              (then best-world-design is open-world with certainty 80))

        (rule (if favourite-world-build-novice is si)
              (then best-world-design is closed-world with certainty 20))

         (rule (if favourite-world-build-novice is si)
              (then best-world-design is indifferente with certainty 20))

        (rule (if favourite-world-build-novice is no)
              (then best-world-design is closed-world with certainty 80))

        (rule (if favourite-world-build-novice is no)
              (then best-world-design is open-world with certainty 20))

        (rule (if favourite-world-build-novice is no)
              (then best-world-design is indifferente with certainty 30))

        (rule (if favourite-world-build-intermediate is delimitata)
              (then best-world-design is closed-world with certainty 80))

        (rule (if favourite-world-build-intermediate is delimitata)
              (then best-world-design is open-world with certainty 20))

        (rule (if favourite-world-build-intermediate is delimitata)
              (then best-world-design is indifferente with certainty 30))

        (rule (if favourite-world-build-intermediate is libera)
              (then best-world-design is open-world with certainty 80))

        (rule (if favourite-world-build-intermediate is libera)
              (then best-world-design is closed-world with certainty 20))

        (rule (if favourite-world-build-intermediate is libera)
              (then best-world-design is indifferente with certainty 30))

        (rule (if favourite-genre is gdr)
              (then best-world-design is open-world with certainty 70))

        (rule (if favourite-genre is gdr)
              (then best-world-design is closed-world with certainty 30))

        (rule (if favourite-genre is fps)
              (then best-world-design is closed-world with certainty 60))

        (rule (if favourite-genre is fps)
              (then best-world-design is open-world with certainty 40))

        ;regole per determinare le migliori meccaniche di gioco e velocità di gameplay

        (rule (if gameplay-style is frenetico)
              (then best-gameplay is fast-paced with certainty 80))

        (rule (if gameplay-style is frenetico)
              (then best-gameplay is balanced-speed with certainty 10))

        (rule (if gameplay-style is frenetico)
              (then best-gameplay is slow-paced with certainty 10))

        (rule (if gameplay-style is bilanciato)
              (then best-gameplay is fast-paced with certainty 10))

        (rule (if gameplay-style is bilanciato)
              (then best-gameplay is balanced-speed with certainty 80))

        (rule (if gameplay-style is bilanciato)
              (then best-gameplay is slow-paced with certainty 10))

        (rule (if gameplay-style is lento)
              (then best-gameplay is fast-paced with certainty 10))

        (rule (if gameplay-style is lento)
              (then best-gameplay is balanced-speed with certainty 10))

        (rule (if gameplay-style is lento)
              (then best-gameplay is slow-paced with certainty 80))

        (rule (if main-game-purpose is relax)
              (then best-gameplay is slow-paced with certainty 40))

        (rule (if main-game-purpose is relax)
              (then best-gameplay is balanced-speed with certainty 40))

        (rule (if main-game-purpose is relax)
              (then best-gameplay is fast-paced with certainty 20))

        (rule (if main-game-purpose is divertimento)
              (then best-gameplay is lively with certainty 50))

        (rule (if main-game-purpose is divertimento)
              (then best-gameplay is funny with certainty 90))

        (rule (if favourite-genre is fps)
              (then best-gameplay is fast-paced with certainty 80))

        (rule (if favourite-genre is fps)
              (then best-gameplay is slow-paced with certainty 10))

        (rule (if favourite-genre is fps)
              (then best-gameplay is balanced-speed with certainty 20))

        (rule (if favourite-genre is rts)
              (then best-gameplay is strategy with certainty 90))

        (rule (if patience is alto and main-game-purpose is sfida)
              (then best-gameplay is repetitive with certainty 30))

        (rule (if patience is alto and main-game-purpose is sfida)
              (then best-gameplay is strategy with certainty 90))

        (rule (if patience is alto and main-game-purpose is sfida)
              (then best-gameplay is slow-paced with certainty 20))

        (rule (if patience is alto and main-game-purpose is sfida)
              (then best-gameplay is dull with certainty 10))

        (rule (if patience is medio and main-game-purpose is sfida)
              (then best-gameplay is strategy with certainty 60))

        ;TODO: add more rules to categorize frustrating, punishing, repetitive and challenging gameplay

        )


;;************************
;;* VIDEOGAMES *
;;************************

(defmodule VIDEOGAMES (import MAIN ?ALL))

(deffacts any-attributes
  (attribute (name best-genre) (value any))
  (attribute (name best-difficulty) (value any))
  (attribute (name best-learning-curve) (value any))
  (attribute (name best-plot) (value any))
  (attribute (name best-plot-feature) (value any))
  (attribute (name best-audio) (value any))
  (attribute (name best-graphics) (value any))
  (attribute (name best-AI) (value any))
  (attribute (name best-gameplay) (value any))
  (attribute (name best-world-design) (value any))
  )

(deftemplate VIDEOGAMES::videogame
        (slot name (default ?NONE))
        (multislot genre (default any))
        (multislot difficulty (allowed-values easy medium hard user-choice) (default ?NONE))
        (slot learning-curve (allowed-values quick-growth s-curve slow-growth linear any) (default any))
        (slot plot (allowed-values awful normal good excellent no-plot))
        (multislot plot-feature (default any))
        (slot audio (allowed-values awful normal good excellent no-audio))
        (slot graphics (allowed-values awful normal good excellent) (default good))
        (multislot AI (allowed-values challenging dumb tanked balanced))
        (multislot gameplay (default any) (allowed-values funny repetitive lively slow-paced fast-paced balanced-speed dull frustrating challenging punishing strategy any))
        (slot world-design (allowed-values open-world closed-world indifferente any) (default indifferente))
        )

(deffacts VIDEOGAMES::the-vg-list
        (videogame (name Bioshock-Infinite) (genre fps action) (difficulty user-choice) (learning-curve quick-growth) (plot excellent) (plot-feature non-linear paradox coherent no-plot-holes) (audio excellent) (graphics excellent) (AI dumb tanked) (gameplay funny repetitive balanced-speed) (world-design closed-world))
        (videogame (name Remember-Me) (genre action platformer) (difficulty user-choice) (learning-curve quick-growth) (plot excellent) (audio excellent) (graphics excellent) (AI dumb challenging) (gameplay funny repetitive balanced-speed) (world-design closed-world))
        (videogame (name Age-of-Empires-II) (genre rts) (difficulty user-choice hard) (learning-curve s-curve) (plot no-plot) (audio good) (graphics good) (AI challenging balanced) (gameplay funny balanced-speed strategy))
        (videogame (name Civilization-V) (genre 4X turn-based-strategy) (difficulty hard user-choice) (learning-curve linear) (plot no-plot) (audio good) (graphics good) (AI challenging balanced) (gameplay funny strategy))
        (videogame (name Sins-Of-Solar-Empire) (genre 4X rts turn-based-strategy) (difficulty user-choice hard) (learning-curve s-curve) (plot good) (audio good) (graphics good) (AI challenging balanced) (gameplay funny strategy))
        (videogame (name Crusader-Kings-2) (genre 4X turn-based-strategy) (difficulty user-choice) (learning-curve s-curve) (plot excellent) (audio excellent) (graphics excellent) (AI challenging balanced) (gameplay funny lively strategy))
        (videogame (name Starcraft-II) (genre rts) (difficulty user-choice hard) (learning-curve s-curve) (plot excellent) (audio excellent) (graphics excellent) (AI challenging balanced) (gameplay funny balanced-speed strategy))
        (videogame (name Far-Cry-3) (genre fps) (difficulty  user-choice) (learning-curve quick-growth) (plot normal) (audio good) (graphics excellent) (AI challenging balanced) (world-design open-world) (gameplay funny lively))
        (videogame (name Call-of-Juarez-Gunslinger) (genre fps) (difficulty user-choice) (learning-curve quick-growth) (plot good) (audio excellent) (graphics excellent) (AI challenging balanced) (world-design closed-world) (gameplay funny lively))
        (videogame (name The-Witcher-3-Wild-Hunt) (genre gdr) (difficulty user-choice) (learning-curve quick-growth) (plot excellent) (audio excellent) (graphics excellent) (AI challenging) (gameplay funny lively) (world-design open-world))
        (videogame (name Dark-Souls) (genre gdr) (difficulty hard) (learning-curve slow-growth) (plot excellent) (audio excellent) (graphics good) (AI challenging) (gameplay funny frustrating punishing slow-paced challenging) (world-design open-world))
        (videogame (name Fahrenheit) (genre avventura-grafica) (difficulty user-choice) (learning-curve quick-growth) (plot excellent) (audio normal) (AI balanced) (gameplay slow-paced) (graphics normal) (world-design closed-world))
        (videogame (name Bastion) (genre platformer) (difficulty user-choice) (learning-curve quick-growth) (plot excellent) (audio excellent) (AI challenging balanced) (graphics excellent) (gameplay funny repetitive) (world-design closed-world))
        (videogame (name The-Walking-Dead) (genre avventura-grafica) (difficulty easy user-choice) (learning-curve quick-growth) (plot excellent) (audio good) (graphics good) (AI balanced) (gameplay slow-paced) (world-design closed-world))
        (videogame (name Beyond-Two-Souls) (genre avventura-grafica) (difficulty easy) (learning-curve quick-growth) (plot excellent) (audio excellent) (graphics excellent) (AI balanced) (gameplay slow-paced balanced-speed) (world-design closed-world))


        )

(defrule VIDEOGAMES::generate-videogames
  (videogame (name ?name)
        (genre $? ?c $?)
        (difficulty $? ?b $?)
        (learning-curve ?s)
        (plot ?p)
        (audio ?a)
        (graphics ?g)
        (AI $? ?ai $?)
        (gameplay $? ?gm $?)
        (world-design ?wd))
  (attribute (name best-genre) (value ?c) (certainty ?certainty-1))
  (attribute (name best-difficulty) (value ?b) (certainty ?certainty-2))
  (attribute (name best-learning-curve) (value ?s) (certainty ?certainty-3))
  (attribute (name best-plot) (value ?p) (certainty ?certainty-p))
  (attribute (name best-audio) (value ?a) (certainty ?certainty-a))
  (attribute (name best-graphics) (value ?g) (certainty ?certainty-g))
  (attribute (name best-AI) (value ?ai) (certainty ?certainty-ai))
  (attribute (name best-gameplay) (value ?gm) (certainty ?certainty-gm))
  (attribute (name best-world-design) (value ?wd) (certainty ?certainty-wd))
  =>
  (assert (attribute (name videogame) (value ?name)
                     (certainty (min ?certainty-1 ?certainty-2 ?certainty-3 ?certainty-p ?certainty-a ?certainty-g ?certainty-ai ?certainty-gm ?certainty-wd)))))
