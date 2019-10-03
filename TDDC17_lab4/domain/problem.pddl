(define	(problem shakeys-world)
	(:domain shakey)
	(:objects
		s		- shakey
	    r1 r2	- room
		d		- door
		ls1 ls2	- light-switch
		b - box
    )

    (:init
	    (at s r1)
        (adjacent r1  r2)
        (box-in b r1)
        (fit-door b d)
	)
    (:goal
        (and (box-in b r2))
    )
    
)