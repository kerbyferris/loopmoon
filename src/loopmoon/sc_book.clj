(ns loopmoon.core
  (:use overtone.live
        overtone.inst.drum
        overtone.synth.retro))

(stop)

(demo 10 (sin-osc (+ 1000 (* 600 (lf-noise0:kr [12 24]))) 0.3))
(demo 30 (rlpf (dust [12 15]) (+ 1600 (* 1500 (lf-noise1 [1/3, 1/4]))) 0.02 ))
(demo 30
      (let [sines 5 speed 6]
        (* (mix (map #(pan2 (* (sin-osc (* % 100))
                               (max 0 (+ (lf-noise1:kr [speed speed]) (line:kr 1 -1 30))))
                            (- (clojure.core/rand 2) 1))
                     (range sines)))
           (/ 1 sines))))

(demo 10 (pm-osc (line:kr 220 440 10) (mouse-y:kr 1 550) (mouse-x:kr 1 15)))
(demo 10 (pm-osc (line:kr 440 110 10) (line:kr 1 550 10) (line:kr 1 15 10)))
(demo 10 (pm-osc (mouse-y:kr 1 550) (mouse-x:kr 1 550) (mouse-x:kr 1 15)))

(demo 60 (*
          (linen:kr (impulse:kr (line:kr 1 20 60)) 0 0.5 (/ 1 (line:kr 1 20 60)))
          (blip:ar
              (t-rand:kr 100 100 (impulse:kr (line:kr 1 20 60)))
              (t-rand:kr 1 10 (impulse:kr (line:kr 1 20 60))))))
(demo 60
      (let [trigger       (line:kr :start 1, :end 20, :dur 60)
            freq          (t-rand:kr :lo 100, :hi 1000, :trig (impulse:kr trigger))
            num-harmonics (t-rand:kr :lo 1,   :hi 10,   :trig (impulse:kr trigger))
            amp           (linen:kr :gate (impulse:kr trigger) :attack-time 0, :sus-level 0.5, :release-time (/ 1 trigger))]
        (* amp (blip freq num-harmonics))))

(demo 10 (pm-osc 100 500 10 0 0.5))

(demo 30 (let [noise (lf-noise1 3)
               saws (mul-add (lf-saw [5, 5.123]) 3 80)
               freq (midicps (mul-add noise 24 saws))
               src (* 0.4 (sin-osc freq))]
           (comb-n src 1 0.3 2)))

(quick-kick)
(closed-hat)
(haziti-clap)
(hat3)

;(demo 10 (tb-303 60 ))

(demo 10
      (let [rate (mouse-x:kr (/ 1 3) 10)
            amp (linen:kr (impulse:kr rate) 0 1 (/ 1 rate))]
          (* amp (sin-osc))))

(demo 10
      (let [rate (line:kr 1 20 60)
            trigger (impulse:kr rate)
            env (linen:kr trigger 0 0.5 (/ 1 rate))
            freq (t-rand:kr 1 10 trigger)]
          (* env (blip (* freq 100) freq))))


(demo 10
      (let [rate (impulse:kr 10)
            carrier (t-rand:kr 100 5000 rate)
            mod-ratio (t-rand:kr 100 5000 rate)]
          (* 0.3 (pm-osc:ar carrier (* carrier mod-ratio)))))

(demo 10
      (let [rate      (line:kr :start 1 :end 20 :dur 10)
            carrier   (* (+ 500 700)
                         (lf-noise0:kr :freq [rate rate]))
            mod-ratio (mouse-x:kr :min 1 :max 2.0)
            env       (linen:kr :attack-time (/ 1 rate)
                                :release-time 0)]
          (* env 
             (pm-osc:ar :car-freq carrier
                        :mod-freq (* carrier mod-ratio
                                     (line:kr :start 1 :end 12 :dur 10))))))

(defsynth left-sine [] (out 0 (sin-osc)))
(left-sine)
(defsynth right-sine [] (out 1 (sin-osc)))
(right-sine)

(defsynth one-tone-only [freq 440]
    (let [src (* 0.3 (sin-osc freq))]
        (out:ar 0 src)))
(one-tone-only 440)
(one-tone-only 220)
(one-tone-only 110)

(defsynth pm-crotale [midi 60 tone 3 art 1 amp 0.8 pan 0]
    (let [freq (midicps midi)
          env (perc 0 art)
          mod (+ 5 (/ 1 (i-rand 2 6)))
          src (pm-osc :car-freq freq
                          :mod-freq (* mod freq)
                          :pm-index (env-gen:kr :envelope env
                                                :time-scale art
                                                :level-scale tone)
                          :mod-phase (env-gen:kr :envelope env
                                           :time-scale art
                                           :level-scale 0.3))
          src (pan2 src pan)
          src (* src
                 (env-gen :envelope env
                          :time-scale (* 1.3 art)
                          :level-scale (ranged-rand 0.1 0.5)
                          :action 2))]
        (out 0 src)))
(pm-crotale :midi (ranged-rand 48 72) :tone (ranged-rand 1 6))

(defsynth pmc-rotale [midi 60 tone 3 art 1 amp 0.8 pan 0]
  (let [freq (midicps midi)
        env (perc 0 art)
        mod (+ 5 (/ 1 (i-rand 2 6)))
        src (* (pm-osc freq (* mod freq) (env-gen:kr env :time-scale art, :level-scale tone) 0)
               (env-gen:kr env :time-scale art, :level-scale 0.3))
        src (pan2 src pan)
        src (* src (env-gen:kr env :time-scale (* art 1.3) :level-scale (ranged-rand 0.1 0.5) :action FREE))]
    (out 0 src)))
(pmc-rotale :midi (ranged-rand 48 72) :tone (ranged-rand 1 6))

(def houston (load-sample "/Applications/SuperCollider/SuperCollider.app/Contents/Resources/sounds/a11wlk01-44_1.aiff"))
(def chooston (load-sample "/Applications/SuperCollider/SuperCollider.app/Contents/Resources/sounds/a11wlk01.wav"))
(def other-sample (load-sample "resources/audio/loop.aif"))

(demo 4 (play-buf 1 houston))
(demo 5 (play-buf 1 chooston))
(demo 5 (play-buf 1 other-sample))

(demo 60
      (let [rate [1 1.01]
            trigger (impulse:kr rate)
            frames (num-frames other-sample)
            src (play-buf 1 other-sample 1 trigger (* frames (line:kr 0 1 60)))
            env (env-gen:kr (lin 0.01 0.96 0.01) trigger)]
          (* src env rate)))

(demo 60
      (let [speed (+ 1 (* 0.2 (lf-noise0:kr 12)))
            direction (lf-clip-noise:kr (/ 1 3))]
          (play-buf 1 chooston (* speed direction) :loop 1)))

(def kbus1 (control-bus))
(def kbus2 (control-bus))

(defsynth src []
      (let [speed (+ 1 (* 0.2 (in:kr kbus1 1)))
            direction (in:kr kbus2)]
          (out 0 (play-buf 1 other-sample (* speed direction) :loop 1))))

(defsynth control1 []
    (out:kr kbus1 (lf-noise0:kr 12)))

(defsynth control2 []
    (out:kr kbus2 (lf-clip-noise:kr (/ 1 4))))

(defsynth player []
    (let [speed (+ 1 (* 0.2 (in:kr kbus1 1)))
          direction (in:kr kbus2)]
        (out 1 (play-buf 1 chooston (* speed direction) :loop 1))))

(do
    (src)
    (control1)
    (control2)
    (player))


(demo 60
      (out 0 (pan2 (* (play-buf 1 other-sample :loop 1)
                      (sin-osc (+ 600 (* 500 (lf-noise0:kr 12))))))
                   0.5))

(demo 60 (let [source (play-buf 1 chooston :loop 1)
               delay (allpass-c :in source 
                                :max-delay-time 2
                                :delay-time [0.65 1.15]
                                :decay-time 10)]
             (+ delay (pan2 source))))

(do
  (def delay-b (audio-bus 2))
  (def mod-b (audio-bus 2))
  (def gate-b (audio-bus 2))
  (def k5-b (control-bus))

  (defsynth control-syn [] (out:kr k5-b (lf-noise0:kr 4)))
  )
