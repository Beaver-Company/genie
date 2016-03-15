/*
 *
 *  Copyright 2015 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.genie.core.services;

import com.github.fge.jsonpatch.JsonPatch;
import com.netflix.genie.common.dto.Command;
import com.netflix.genie.common.dto.CommandStatus;
import com.netflix.genie.common.dto.Cluster;
import com.netflix.genie.common.dto.ClusterStatus;
import com.netflix.genie.common.dto.JobRequest;
import com.netflix.genie.common.exceptions.GenieException;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Abstraction layer to encapsulate data ClusterConfig functionality.<br>
 * Classes implementing this abstraction layer must be thread-safe
 *
 * @author amsharma
 * @author tgianos
 */
@Validated
public interface ClusterService {

    /**
     * Create new cluster configuration.
     *
     * @param cluster The cluster to create
     * @return The created clusters id
     * @throws GenieException if there is an error
     */
    String createCluster(
            @NotNull(message = "No cluster entered. Unable to create.")
            @Valid
            final Cluster cluster
    ) throws GenieException;

    /**
     * Get the cluster configuration by id.
     *
     * @param id unique id of cluster configuration to return
     * @return The cluster configuration
     * @throws GenieException For any error
     */
    Cluster getCluster(
            @NotBlank(message = "No id entered. Unable to get.")
            final String id
    ) throws GenieException;

    /**
     * Get cluster info for various parameters. Null or empty parameters are
     * ignored.
     *
     * @param name          cluster name
     * @param statuses      valid types - Types.ClusterStatus
     * @param tags          tags allocated to this cluster
     * @param minUpdateTime min time when cluster configuration was updated
     * @param maxUpdateTime max time when cluster configuration was updated
     * @param page          The page to get
     * @return All the clusters matching the criteria
     */
    Page<Cluster> getClusters(
            final String name,
            final Set<ClusterStatus> statuses,
            final Set<String> tags,
            final Date minUpdateTime,
            final Date maxUpdateTime,
            final Pageable page
    );

    /**
     * Get the clusters on which the job can be run.
     *
     * @param jobRequest The request to runt he job. Not null.
     * @return successful response, or one with HTTP error code
     * @throws GenieException if there is an error
     */
    List<Cluster> chooseClusterForJobRequest(
            @NotNull(message = "JobRequest object is null. Unable to continue.")
            final JobRequest jobRequest
    ) throws GenieException;

    /**
     * Update a cluster.
     *
     * @param id            The id of the cluster to update
     * @param updateCluster The information to update the cluster with
     * @throws GenieException if there is an error
     */
    void updateCluster(
            @NotBlank(message = "No cluster id entered. Unable to update.")
            final String id,
            @NotNull(message = "No cluster information entered. Unable to update.")
            @Valid
            final Cluster updateCluster
    ) throws GenieException;

    /**
     * Patch a cluster with the given json patch.
     *
     * @param id    The id of the cluster to update
     * @param patch The json patch to use to update the given cluster
     * @throws GenieException if there is an error
     */
    void patchCluster(@NotBlank final String id, @NotNull final JsonPatch patch) throws GenieException;

    /**
     * Delete all clusters from database.
     *
     * @throws GenieException if there is an error
     */
    void deleteAllClusters() throws GenieException;

    /**
     * Delete a cluster configuration by id.
     *
     * @param id unique id for cluster to delete
     * @throws GenieException if there is an error
     */
    void deleteCluster(
            @NotBlank(message = "No id entered unable to delete.")
            final String id
    ) throws GenieException;

    /**
     * Add configuration files to the cluster.
     *
     * @param id      The id of the cluster to add the configuration file to. Not
     *                null/empty/blank.
     * @param configs The configuration files to add. Not null/empty.
     * @throws GenieException if there is an error
     */
    void addConfigsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to add configurations.")
            final String id,
            @NotEmpty(message = "No configuration files entered. Unable to add.")
            final Set<String> configs
    ) throws GenieException;

    /**
     * Get the set of configuration files associated with the cluster with given
     * id.
     *
     * @param id The id of the cluster to get the configuration files for. Not
     *           null/empty/blank.
     * @return The set of configuration files as paths
     * @throws GenieException if there is an error
     */
    Set<String> getConfigsForCluster(
            @NotBlank(message = "No cluster id sent. Cannot retrieve configurations.")
            final String id
    ) throws GenieException;

    /**
     * Update the set of configuration files associated with the cluster with
     * given id.
     *
     * @param id      The id of the cluster to update the configuration files for.
     *                Not null/empty/blank.
     * @param configs The configuration files to replace existing configurations
     *                with. Not null/empty.
     * @throws GenieException if there is an error
     */
    void updateConfigsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to update configurations.")
            final String id,
            @NotEmpty(message = "No configs entered. Unable to update.")
            final Set<String> configs
    ) throws GenieException;

    /**
     * Remove all configuration files from the cluster.
     *
     * @param id The id of the cluster to remove the configuration file from.
     *           Not null/empty/blank.
     * @throws GenieException if there is an error
     */
    void removeAllConfigsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to remove configs.")
            final String id
    ) throws GenieException;

    /**
     * Add tags to the cluster.
     *
     * @param id   The id of the cluster to add the tags to. Not
     *             null/empty/blank.
     * @param tags The tags to add. Not null/empty.
     * @throws GenieException if there is an error
     */
    void addTagsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to add tags.")
            final String id,
            @NotEmpty(message = "No tags entered. Unable to add to tags.")
            final Set<String> tags
    ) throws GenieException;

    /**
     * Get the set of tags associated with the cluster with given
     * id.
     *
     * @param id The id of the cluster to get the tags for. Not
     *           null/empty/blank.
     * @return The set of tags as paths
     * @throws GenieException if there is an error
     */
    Set<String> getTagsForCluster(
            @NotBlank(message = "No cluster id sent. Cannot retrieve tags.")
            final String id
    ) throws GenieException;

    /**
     * Update the set of tags associated with the cluster with
     * given id.
     *
     * @param id   The id of the cluster to update the tags for.
     *             Not null/empty/blank.
     * @param tags The tags to replace existing tags
     *             with. Not null/empty.
     * @throws GenieException if there is an error
     */
    void updateTagsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to update tags.")
            final String id,
            @NotEmpty(message = "No tags entered. Unable to update.")
            final Set<String> tags
    ) throws GenieException;

    /**
     * Remove all tags from the cluster.
     *
     * @param id The id of the cluster to remove the tags from.
     *           Not null/empty/blank.
     * @throws GenieException if there is an error
     */
    void removeAllTagsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to remove tags.")
            final String id
    ) throws GenieException;

    /**
     * Remove a tag from the cluster.
     *
     * @param id  The id of the cluster to remove the tag from. Not
     *            null/empty/blank.
     * @param tag The tag to remove. Not null/empty/blank.
     * @throws GenieException if there is an error
     */
    void removeTagForCluster(
            @NotBlank(message = "No cluster id entered. Unable to remove tag.")
            final String id,
            @NotBlank(message = "No tag entered. Unable to remove.")
            final String tag
    ) throws GenieException;

    /**
     * Add commands to the cluster.
     *
     * @param id         The id of the cluster to add the command file to. Not
     *                   null/empty/blank.
     * @param commandIds The ids of the commands to add. Not null/empty.
     * @throws GenieException if there is an error
     */
    void addCommandsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to add commands.")
            final String id,
            @NotEmpty(message = "No command ids entered. Unable to add commands.")
            final List<String> commandIds
    ) throws GenieException;

    /**
     * Get the set of commands associated with the cluster with given id.
     *
     * @param id       The id of the cluster to get the commands for. Not
     *                 null/empty/blank.
     * @param statuses The statuses to get commands for
     * @return The list of commands
     * @throws GenieException if there is an error
     */
    List<Command> getCommandsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to get commands.")
            final String id,
            final Set<CommandStatus> statuses
    ) throws GenieException;

    /**
     * Update the set of command files associated with the cluster with
     * given id.
     *
     * @param id         The id of the cluster to update the command files for. Not
     *                   null/empty/blank.
     * @param commandIds The ids of the commands to replace existing
     *                   commands with. Not null/empty.
     * @throws GenieException if there is an error
     */
    void updateCommandsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to update commands.")
            final String id,
            @NotNull(message = "No command ids entered. Unable to update commands.")
            final List<String> commandIds
    ) throws GenieException;

    /**
     * Remove all commands from the cluster.
     *
     * @param id The id of the cluster to remove the commands from. Not
     *           null/empty/blank.
     * @throws GenieException if there is an error
     */
    void removeAllCommandsForCluster(
            @NotBlank(message = "No cluster id entered. Unable to remove commands.")
            final String id
    ) throws GenieException;

    /**
     * Remove a command from the cluster.
     *
     * @param id    The id of the cluster to remove the command from. Not
     *              null/empty/blank.
     * @param cmdId The id of the command to remove. Not null/empty/blank.
     * @throws GenieException if there is an error
     */
    void removeCommandForCluster(
            @NotBlank(message = "No cluster id entered. Unable to remove command.")
            final String id,
            @NotBlank(message = "No command id entered. Unable to remove command.")
            final String cmdId
    ) throws GenieException;
}
