import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRoomHotel } from 'app/shared/model/room-hotel.model';
import { getEntities as getRoomHotels } from 'app/entities/room-hotel/room-hotel.reducer';
import { getEntity, updateEntity, createEntity, reset } from './image-room.reducer';
import { IImageRoom } from 'app/shared/model/image-room.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ImageRoomUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const roomHotels = useAppSelector(state => state.roomHotel.entities);
  const imageRoomEntity = useAppSelector(state => state.imageRoom.entity);
  const loading = useAppSelector(state => state.imageRoom.loading);
  const updating = useAppSelector(state => state.imageRoom.updating);
  const updateSuccess = useAppSelector(state => state.imageRoom.updateSuccess);
  const handleClose = () => {
    props.history.push('/image-room');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRoomHotels({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...imageRoomEntity,
      ...values,
      roomHotel: roomHotels.find(it => it.id.toString() === values.roomHotel.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...imageRoomEntity,
          roomHotel: imageRoomEntity?.roomHotel?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="doantaiApp.imageRoom.home.createOrEditLabel" data-cy="ImageRoomCreateUpdateHeading">
            Create or edit a ImageRoom
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="image-room-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Link Image" id="image-room-linkImage" name="linkImage" data-cy="linkImage" type="text" />
              <ValidatedField id="image-room-roomHotel" name="roomHotel" data-cy="roomHotel" label="Room Hotel" type="select">
                <option value="" key="0" />
                {roomHotels
                  ? roomHotels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/image-room" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ImageRoomUpdate;
