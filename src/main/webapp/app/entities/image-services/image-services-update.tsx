import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IServices } from 'app/shared/model/services.model';
import { getEntities as getServices } from 'app/entities/services/services.reducer';
import { getEntity, updateEntity, createEntity, reset } from './image-services.reducer';
import { IImageServices } from 'app/shared/model/image-services.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ImageServicesUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const services = useAppSelector(state => state.services.entities);
  const imageServicesEntity = useAppSelector(state => state.imageServices.entity);
  const loading = useAppSelector(state => state.imageServices.loading);
  const updating = useAppSelector(state => state.imageServices.updating);
  const updateSuccess = useAppSelector(state => state.imageServices.updateSuccess);
  const handleClose = () => {
    props.history.push('/image-services');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getServices({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...imageServicesEntity,
      ...values,
      services: services.find(it => it.id.toString() === values.services.toString()),
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
          ...imageServicesEntity,
          services: imageServicesEntity?.services?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="doantaiApp.imageServices.home.createOrEditLabel" data-cy="ImageServicesCreateUpdateHeading">
            Create or edit a ImageServices
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="image-services-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Linkimage" id="image-services-linkimage" name="linkimage" data-cy="linkimage" type="text" />
              <ValidatedField id="image-services-services" name="services" data-cy="services" label="Services" type="select">
                <option value="" key="0" />
                {services
                  ? services.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/image-services" replace color="info">
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

export default ImageServicesUpdate;
