package com.isep.acme.repositories;

import com.isep.acme.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface VoteRepository extends CrudRepository<Vote, UUID> {

    @Query("SELECT v FROM Vote v WHERE v.voteID=:voteID")
    Optional<Vote> findByID(UUID voteID);

    //Delete the vote when given the voteID
    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.voteID=:voteID")
    void deleteByVoteID(@Param("voteID") UUID voteID);

    //Update the vote when given the voteID
    @Transactional
    @Modifying
    @Query("UPDATE Vote v SET v.voteID = :voteID WHERE v.voteID=:voteID")
    Vote updateByVoteID(@Param("voteID") UUID voteID);

    @Query("SELECT v FROM Vote v WHERE v.voteID=:voteID")
    Optional<Vote> findById(UUID voteID);

}
