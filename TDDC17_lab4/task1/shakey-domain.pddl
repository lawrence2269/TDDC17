;; This is the domain for shakey

(define (domain shakey)
	(:requirements 
		:strips
		:typing
		:equality
	)

	(:types
		box				; boxes that can be pushed
		switch 			; turn on/off light
		room 			; there are several connected rooms
		shakey 			; the robot
		object 			; small objects that can be lifted by robot
		gripper			; one of the hands
	)

	;; The predicates we have chosen are what we need for the actions.
    ;; We have simplified the code i.e we didn't care about whether the box is under the switch,
    ;; but to make sure that box is in the room.

	(:predicates
		(adjacent		?r1	?r2 - room)			; can move from ?r1 directly to ?r2
		(wide-entrance	?r1 ?r2 - room)			; is there a wide door in between ?r1 and ?r2
		
		(box-at			?b	- box ?r - room) 	; box ?b1 is in room ?r
		(shakey-at		?s - shakey ?r - room)  ; is shakey ?s in room ?r
		(switch-at		?s - switch ?r - room)	; is switch ?s in room ?r
		(object-at		?o - object ?r - room)	; is there small objects ?o in room ?r
		
		(light			?r - room)				; is room ?r lit 
		
		(holding		?c - gripper 	?o - object); is gripper ?c holdig object ?o
		(empty			?c - gripper)				; is gripper ?c empty 
	)

    ;; We have written precondition checks for room-adjacent, i.e. r1 and r2 is adjacent OR r2 and r1
    ;; adjacent. In the problem, we will just describe to shakey that r1 is adjacent to r2, hence need to 
    ;; complicate the problem file.

	;; This action "move" will move shakey from one room to another, the rooms have to be adjacent
	;; and the shakey must be in the room at initial state.
	(:action move
		:parameters		(?s - shakey	?from ?to - room)

		:precondition 	(and (shakey-at 	?s ?from)
							(or (adjacent 	?from ?to)
								(adjacent 	?to ?from)
							)
						)

		:effect			(and (shakey-at	?s ?to)
						(not (shakey-at	?s ?from))
						)
	)

	;; This action will turn lights on in a room and for this to happen as per lab conditions,
	;; we need a box to be in the room.
	;; We also make sure that the light is off and lightswitch is present.
	(:action lights-on
		:parameters		(?s - shakey	?sw - switch 	?r - room 	?b - box)

		:precondition	(and (shakey-at	?s ?r)
						 (switch-at ?sw ?r)
						 (box-at	?b  ?r)
						 (not (light ?r))
						)

		:effect			(light ?r)
	)

	;; This action will turn lights off in  a room and for this to happen as per lab conditions,
    ;; we need a box to be in the room. 
    ;; We also make sure that the light is turned on in the room. 
    ;; In this action, we no need to look for light switches, just need to look whether light is turned on.

	(:action turn-light-off
		:parameters		(?s - shakey	?r - room 	?b - box)

		:precondition	(and (shakey-at	?s ?r)
						 (box-at	?b  ?r)
						 (light ?r)
						)

		:effect			(not (light ?r))
	)

	;; This action will move a box from one room to another room. The rooms have to be 
	;; adjacent as well as wider for this to happen. 
    ;; Shakey has to be in the same room where the box is present.
	(:action move-box
		:parameters		(?s - shakey 	?b - box 	?from ?to - room)

		:precondition	(and (shakey-at	?s 	?from)
							 (box-at 	?b 	?from)
							(or (adjacent 	?from ?to)
								(adjacent 	?to ?from)
							)
							(or (wide-entrance 	?from ?to)
							(wide-entrance 	?to ?from)
							)
						)

		:effect			(and (shakey-at 	?s 	?to)
							 (box-at 	?b 	?to)
							 (not (box-at 	?b 	?from))
							 (not (shakey-at ?s 	?from))
						)
	)

	;; This action will pick up an object only if shakey has a pair of grippers which is free 
	;; and the lights are switched on in the room where we are going to pick up the objects and
    ;; like move-box action, shakey has to be in the same room where the object is present. 
	(:action pick-up
		:parameters 	(?s - shakey 	?r - room 	?c - gripper 	?o - object)

		:precondition 	(and (shakey-at 	?s 	?r)
							 (object-at ?o 	?r)
							 (light 	?r)
							 (empty 	?c)
						)

		:effect			(and (not(object-at ?o 	?r))
							 (holding	?c 	?o)
							 (not (empty ?c))
						)
	)

	;; This action will put down an object which it is holding and for this to happen 
    ;; One of the shakey's gripper has to hold an object.
    ;; This action does not require light to be switched on in the room to put down an object.
	
	(:action put-down
		:parameters 	(?s - shakey 	?r - room 	?c - gripper 	?o - object)

		:precondition 	(and (shakey-at 	?s 	?r)
							 (holding ?c ?o)
						)

		:effect			(and (object-at ?o 	?r)
							 (not (holding	?c 	?o))
							 (empty ?c)
						)
	)
)