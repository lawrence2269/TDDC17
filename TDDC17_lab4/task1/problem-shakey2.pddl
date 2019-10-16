;; This is a problem for shakey domain
;; We are going to increase number of object to be moved and number of rooms in this problem.
;; we are going to put all objects in the first room and shakey in the same room.
;; the goal is to move all objects from the first room to last room. 

(define (problem task2)
  (:domain shakey)
  (:objects 
  			ro1 ro2 ro3 ro4 ro5 ro6 ro7 ro8 ro9 ro10 	  - room
  			s 		 	                                   - shakey
  			sw1                                         - switch
  			b			                                   - box
  			g1 g2		                                   - gripper
  			o1 o2 o3 o4 o5 o6 o7 o8 o9 o10              - object

  )

  (:init 
  		(adjacent ro1 ro2)	(adjacent ro2 ro3)
        (adjacent ro3 ro4)    (adjacent ro4 ro5) 
        (adjacent ro5 ro6)    (adjacent ro6 ro7)   
        (adjacent ro7 ro8)    (adjacent ro8 ro9) (adjacent ro9 ro10) 

        (shakey-at s ro1)		(box-at	b ro1) (empty g1) (empty g2) (switch-at sw1 ro1) 
  			
  		(object-at	o1 ro1) (object-at o2 ro1) (object-at o3 ro1) (object-at o4 ro1) (object-at o5 ro1)
        (object-at  o6 ro1) (object-at o7 ro1) 
        (object-at o8 ro1) (object-at o9 ro1) (object-at o10 ro1)
  			
  )
  (:goal 
  		(and (object-at o1 ro10) (object-at o2 ro10) (object-at o3 ro10) 
            (object-at o4 ro10) (object-at o5 ro10) 
            (object-at  o6 ro10) (object-at o7 ro10) 
            (object-at o8 ro10) 
            (object-at o9 ro10) (object-at o10 ro10)
      )
  )
)