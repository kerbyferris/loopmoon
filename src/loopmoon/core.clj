(ns loopmoon.core
  (:use overtone.live))

(stop)

(demo 10
      (lpf (mix (saw [400 (line 100 1600 5) 101 100.5]))
           (lin-lin (lf-tri (line 2 20 5)) -1 1 400 4000)))
