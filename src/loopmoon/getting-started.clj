(ns loopmoon.core
  (:use overtone.live))

(stop)

(demo 10
      (lpf (mix (saw [400 (line 100 1600 5) 101 100.5]))
           (lin-lin (lf-tri (line 2 20 5)) -1 1 400 4000)))

(definst foo [freq 220] (* 0.3 (saw freq)))
(foo)

(definst trem [freq 432 depth 10 rate 6 length 13]
  (* 0.3
     (line:kr 0 1 length FREE)
     (saw (+ freq (* depth (sin-osc:kr rate))))))

(trem 200 60 0.8)
(trem 60 30 0.2)
(ctl trem :freq 428)

(demo 10 (example dbrown :rand-walk))
(demo 10 (example iir-filter :low-pass))

(ctl foo :freq 432)

(definst sin-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc freq)
     vol))
(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))
(definst square-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-pulse:ar freq)
     vol))
(definst noisey [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (pink-noise)
     vol))
(definst triangle-wave [freq 440 attack 0.01 sustain 0.1 release 0.4 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-tri freq)
     vol))
(triangle-wave)
(noisey)
(square-wave)
(saw-wave)
(sin-wave)

(spooky-house)
(definst spooky-house [freq 440 width 0.2
                       attack 0.3 sustain 4 release 0.9
                       vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc (+ freq (* 20 (lf-pulse:kr 0.5 0 width))))
     vol))

