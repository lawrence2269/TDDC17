(define (domain shakey)
  (:requirements :strips :equality :typing)
  (:types 
        room  
        light-switch
        box
        door
        shakey 
  )

  (:predicates
      (adjacent	?r1 ?r2 - room)		; can move from ?r1 directly to ?r2
      ;(attached	?p - pile ?l - location)	; pile ?p attached to location ?l
      ;(belong		?k - crane ?l - location)	; crane ?k belongs to location ?l

      (at	?s - shakey ?r - room)	; shakey ?s is in room ?r
      ;(occupied	?l - location)			; there is a robot at location ?l
      ;(pushing ?s - shakey ?b - box )	; shakey ?s is pushing box ?b
      
      (switch	?s - shakey ?ls - light-switch)			; shakey ?s switches a light-switch ?ls
      (switch-on ?ls - light-switch)			; light-switch ?ls is on
      (switch-in ?ls - light-switch ?r - room)

      (box-under	?b - box ?ls - light-switch)	; box ?b is under light-switch ?ls
      (no-box	?ls - light-switch)			; light-switch ?ls does not have a box under it

      (box-in		?b - box ?r - room)	; box ?b is somewhere in room ?r
      (fit-door ?b - box ?d - door) ; box ?b fits through door ?d
      ;(top		?c - container ?p - pile)	; container ?c is on top of pile ?p
      ;(on		?k1 ?k2 - container )		; container ?k1 is on container ?k2
   )

  (:action move                                
		:parameters (?s - shakey ?from ?to - room)

		:precondition (and (adjacent ?from ?to)
				         (at ?s ?from))

		:effect (and (at ?s ?to) (not (at ?s ?from)))
  )

  (:action push-to-room
    :parameters (?s - shakey ?from ?to - room ?b - box ?d - door)

		:precondition (and (adjacent ?from ?to)
				         (at ?s ?from) (box-in ?b ?from)
                 (fit-door ?b ?d))

		:effect (and (at ?s ?to) (not (at ?s ?from))
            (box-in ?b ?to) (not (box-in	?b ?from)))
  )

  (:action push-to-light-switch
    :parameters (?s - shakey ?b - box ?ls - light-switch ?r - room)

    :precondition (and (box-in ?b ?r) 
                  (no-box ?ls)
                  (switch-in ?ls ?r)
                  (at	?s ?r))
    
    :effect (and (not (no-box ?ls))
            (box-under	?b ?ls))
  )

  (:action turn-on-light
    :parameters (?s - shakey ?ls - light-switch ?r - room)

    :precondition (and (switch-in ?ls ?r)
                  (not (switch-on ?ls))
                  (at	?s ?r)
                  (not (no-box	?ls)))
    
    :effect (and (switch-on ?ls))
  )

)